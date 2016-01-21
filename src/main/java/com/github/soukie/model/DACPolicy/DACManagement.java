package com.github.soukie.model.DACPolicy;

import com.github.soukie.model.DACPolicy.objects.ACLObject;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.DACPolicy.objects.BlackToken;
import com.github.soukie.model.DACPolicy.objects.Capability;
import com.github.soukie.model.ModelValues;
import com.github.soukie.model.database.DatabaseOperation;
import com.github.soukie.model.security.SecurityEncode;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;

/**
 * Class to management DAC policy's subjects, objects,
 * and granting capabilities with three different ways as
 * centralized_acl_policy, distributed_acl_policy, limited_distributed_acl_policy.
 * This three way will be short called Centralized, Distributed and LimitedDistributed.
 * This methods in the class must have a dac database operation to change data.
 * Created by qiyiy on 2016/1/9.
 */
public class DACManagement {
    private DatabaseOperation databaseOperation;

    public DACManagement() {
        databaseOperation = new DatabaseOperation(new Date().getTime());
        try {
            databaseOperation.initDatabaseConnection(ModelValues.DATABASE_MYSQL_PROPERTIES_FILE_PATH);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public DACManagement(DatabaseOperation databaseOperation) {
        this.databaseOperation = databaseOperation;
    }

    /**
     * The method to create subject record to database.
     *
     * @param createdSubject: subject will be create to database
     * @return 0: created failed; 1: created succeed
     */
    public int createSubject(ACLSubject createdSubject) {
        try {
            return databaseOperation.addSubject(createdSubject.getId(),
                    createdSubject.getName(),
                    SecurityEncode.encoderByMd5(SecurityEncode.encoderByMd5(createdSubject.getPassword())),
                    createdSubject.getInfo(),
                    createdSubject.getCreatedTime(),
                    createdSubject.getLastUpdateTime());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e1) {
            e1.printStackTrace();
            return 0;
        }
    }

    /**
     * The method will delete a subject record in database.
     * And will remove all capabilities about subject.
     *
     * @param id: subject's id
     * @return 0: deleted failed; >0: deleted succeed
     */
    public int deleteSubject(int id) {
        int deleteSubjectResult = databaseOperation.deleteSubject(id);
        int deleteCapabilitiesBySubjectIdResult = databaseOperation.deleteCapabilityBySubjectId(id);
        int deleteCapabilitiesByGrantedSubjectIdResult = databaseOperation.deleteCapabilityByGrantedSubjectId(id);
        return deleteCapabilitiesByGrantedSubjectIdResult | deleteCapabilitiesBySubjectIdResult | deleteSubjectResult;
    }

    /**
     * The method to modify a subject record in database according subject's id.
     *
     * @param id:             subject's id
     * @param name:           subject's name
     * @param password:       subject's password
     * @param info:           subject's info
     * @param lastUpdateTime: last update time
     * @return 0: modified failed; >0: modified succeed
     */
    public int modifySubject(int id, String name, String password, String info, long lastUpdateTime) {
        return databaseOperation.modifySubject(id, name, password, info, lastUpdateTime);
    }

    public ACLSubject queryOneSubject(ACLSubject subject) {
        return databaseOperation.queryOneSubject(subject.getId());
    }

    public int querySubjectIdByName(ACLSubject subject) {
        return databaseOperation.querySubjectIdByName(subject.getName());
    }

    public ArrayList<ACLSubject> queryAllSubjects() {
        return databaseOperation.queryAllSubjects();
    }


    /**
     * The method to create a object record in database by subject.
     *
     * @param subject: subject
     * @param object:  object
     * @return 0: created failed; 1: created object succeed but created self capability failed
     * 2: created object and self capability succeed.
     */
    public int createObject(ACLSubject subject, ACLObject object) {
        int createSeletCapabilityResult = 0;
        int addObjectResult = databaseOperation.addObject(object.getId(),
                object.getName(),
                object.getInfo(),
                subject.getId(),
                subject.getName(),
                object.getCreateTime(),
                object.isExecutable());
        if (addObjectResult == 1) {
            createSeletCapabilityResult = createSelfCapability(subject, object, "orwcd");
        }
        return addObjectResult == 0 ? 0 : (createSeletCapabilityResult == 0 ? 1 : 2);
    }

    /**
     * The method to delete a object by id, and remove all capabilities based on subject.
     *
     * @param objectId: object's id
     * @return 0: delete failed 1: deleted succeed
     */
    public int deleteObject(int objectId) {
        int deleteObjectResult = databaseOperation.deleteObject(objectId);
        int deleteCapabilitiesByObjectIdResult = 0;
        if (deleteObjectResult == 1) {
            deleteCapabilitiesByObjectIdResult = databaseOperation.deleteCapabilityByObjectId(objectId);
        }
        return deleteCapabilitiesByObjectIdResult | deleteObjectResult;
    }


    /**
     * The method to modify object's values includes name, info, lastUpdateTime and executeable
     *
     * @param objectId:       object
     * @param name:           new name
     * @param info:           new info
     * @param lastUpdateTime: last update time
     * @param executable:     executeable
     * @return
     */
    public int modifyObject(int objectId, String name, String info, long lastUpdateTime, boolean executable) {
        return databaseOperation.modifyObject(objectId, name, info, lastUpdateTime, executable);
    }

    public ACLObject queryOneObject(ACLObject object) {
        return databaseOperation.queryOneObject(object.getId());
    }

    /**
     * The method to create a capability record to database.
     *
     * @param grantSubject: subject will grant capability
     * @param subject:      subject will be granted capability
     * @param object:       subject with capability
     * @return 0: created failed 1: created succeed 2:there is a black token 3: granted subject has not control of object;
     * 4: have cyclical capability; 5: granted subject = subject:error; 6: the granted subject haven't the capabilities
     * like capability string of object
     */
    public int createCapability(ACLSubject grantSubject, ACLSubject subject, ACLObject object, String capabilityString) {
        if (grantSubject.getId() == subject.getId()) {
            return 5;
        }
        if (!databaseOperation.ifSubjectHaveControlOfObject(grantSubject.getId(), object.getId())) {
            return 3;
        }
        if (!databaseOperation.ifSubjectHaveCapabilitiesOfObject(grantSubject.getId(), object.getId(), capabilityString)) {
            return 6;
        }
        BlackToken blackToken = databaseOperation.queryBlackTokenByObjectIdGrantedSubjectIdSubjectId(object.getId(),
                grantSubject.getId(),
                subject.getId(),
                capabilityString);
        if (blackToken != null) {
            if (blackToken.isBlackToken()) {
                return 2;
            }
        }
        if (capabilityString.charAt(3) == 'c') {
            if (databaseOperation.ifCyclicalCapability(grantSubject.getId(), subject.getId(), object)) {
                return 4;
            }
        }
        return createCapabilityDatabaseOperation(grantSubject, subject, object, capabilityString);

    }

    private int createSelfCapability(ACLSubject subject, ACLObject object, String capabilityString) {
        return createCapabilityDatabaseOperation(subject, subject, object, capabilityString);
    }

    private int createCapabilityDatabaseOperation(ACLSubject grantedSubject,
                                                  ACLSubject subject,
                                                  ACLObject object,
                                                  String capabilityString) {
        Capability capability = new Capability(grantedSubject.getId() * 1000000 + subject.getId() * 1000 +
                object.getId() + CapabilityList.capabilityStringToIntValue(capabilityString),

                object.getId(),
                object.getName(),
                grantedSubject.getId(),
                grantedSubject.getName(),
                subject.getId(),
                subject.getName(),
                new Date().getTime(),
                capabilityString);
        return databaseOperation.addCapability(capability.getCapabilityId(),
                capability.getObjectId(),
                capability.getObjectName(),
                capability.getGrantedSubjectId(),
                capability.getGrantedSubjectName(),
                capability.getSubjectId(),
                capability.getSubjectName(),
                capability.getCreatedTime(),
                capability.getLastUpdateTime(),
                capability.getCapabilityString(),
                capability.getCapabilityInfo());
    }

    /**
     * The method to delete capability record according granted subject's id, subject's id, object's id and capability string.
     * And will deleted all capabilities called capability string granted by subject.
     *
     * @param grantedSubjectId: granted subject's id
     * @param subjectId:        subject's id
     * @param objectId:         object's id
     * @param capabilityString: capability string
     * @return 0: deleted failed 1: deleted succeed; 2: the capability record is not existed
     */
    public int deleteCapability(int grantedSubjectId, int subjectId, int objectId, String capabilityString) {
        int deleteCapabilityResult = databaseOperation.deleteCapabilityByGSSOCSId(grantedSubjectId,
                subjectId,
                objectId,
                capabilityString);
        if (deleteCapabilityResult == 1) {
            databaseOperation.deleteCapabilityByGrantedSubjectIdObjectIdCapabilityString(subjectId, objectId, capabilityString);
        }
        return deleteCapabilityResult;
    }

    /**
     * The method to delete capability record according granted subject's name, subject's name, and object's name adn capability string
     *
     * @param grantedSubject: granted subject
     * @param subject:        subject
     * @param object:         object
     * @param capabilityString:   capability string
     * @return 0: deleted failed 1: deleted succeed; 2: the capability record don't existed
     */
    public int deleteCapability(ACLSubject grantedSubject, ACLSubject subject, ACLObject object, String capabilityString) {
        return deleteCapability(grantedSubject.getId(),
                subject.getId(),
                object.getId(),
                capabilityString);
    }

    /**
     * The method to modify capability by id
     *
     * @param capabilityId:        capability's id
     * @param lastUpdateTime:      last update time
     * @param newCapabilityString: new capability string
     * @param newCapabilityInfo:   new capability information
     * @return
     */
    public int modifyCapability(int capabilityId,
                                long lastUpdateTime,
                                String newCapabilityString,
                                String newCapabilityInfo) {
        return databaseOperation.modifyCapability(capabilityId, lastUpdateTime, newCapabilityString, newCapabilityInfo);
    }

    /**
     * The method to create a black token record
     *
     * @param grantedSubject:   granted subject
     * @param subject:          subject
     * @param object:           object
     * @param capabilityString: capability string
     * @param blackToken:       black token true or false
     * @return 0: created failed; 1: created succeed; 2: the black token record is already existed
     */
    public int createBlackToken(ACLSubject grantedSubject,
                                ACLSubject subject,
                                ACLObject object,
                                String capabilityString,
                                boolean blackToken) {

        return databaseOperation.addBlackToken(grantedSubject.getId() * 1000000 + subject.getId() * 1000 +
                        object.getId() + CapabilityList.capabilityStringToIntValue(capabilityString),

                object.getId(),
                grantedSubject.getId(),
                subject.getId(),
                new Date().getTime(),
                new Date().getTime(),
                capabilityString,
                blackToken);
    }

    /**
     * The method to delete a black token record
     *
     * @param blackTokenId: black token's id
     * @return 0: deleted failed; 1: deleted succeed
     */
    public int deleteBlackToken(int blackTokenId) {
        return databaseOperation.deleteBlackToken(blackTokenId);
    }

    /**
     * The method to delete black token by granted subject subject object amd capability string
     *
     * @param grantedSubject: granted subject
     * @param subject:        subject
     * @param object:         object
     * @param capabilityString: capability string
     * @return 0: deleted failed; 1: deleted succeed; 2: there is no black token record
     */
    public int deleteBlackToken(ACLSubject grantedSubject, ACLSubject subject, ACLObject object, String capabilityString) {
        return databaseOperation.deleteBlackToken(grantedSubject.getId(), subject.getId(),object.getId(), capabilityString);
    }

    /**
     * The method to modify black token with new capability string or black token true or false by black token's id
     *
     * @param blackTokenId:     black token's id
     * @param capabilityString: capability string
     * @param blackToken:       black token true or false
     * @return 0: modified failed; 1: modified succeed
     */
    public int modifyBlackToken(int blackTokenId, String capabilityString, boolean blackToken) {
        return databaseOperation.modifyBlackToken(blackTokenId, capabilityString, blackToken);
    }


}
