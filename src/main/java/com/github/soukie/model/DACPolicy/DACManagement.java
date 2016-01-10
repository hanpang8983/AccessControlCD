package com.github.soukie.model.DACPolicy;

import com.github.soukie.model.DACPolicy.objects.ACLObject;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.DACPolicy.objects.Capability;
import com.github.soukie.model.database.DACDatabaseOperation;
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
    private DACDatabaseOperation dacDatabaseOperation;

    public DACManagement(DACDatabaseOperation dacDatabaseOperation) {
        this.dacDatabaseOperation = dacDatabaseOperation;
    }

    /**
     * The method to create subject record to database.
     *
     * @param createdSubject: subject will be create to database
     * @return 0: created failed; 1: created succeed
     */
    public int createSubject(ACLSubject createdSubject) {
        try {
            return dacDatabaseOperation.addSubject(createdSubject.getId(),
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
        int deleteSubjectResult = dacDatabaseOperation.deleteSubject(id);
        int deleteCapabilitiesBySubjectIdResult = dacDatabaseOperation.deleteCapabilityBySubjectId(id);
        int deleteCapabilitiesByGrantedSubjectIdResult = dacDatabaseOperation.deleteCapbilityByGrantedSubjectId(id);
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
        return dacDatabaseOperation.modifySubject(id, name, password, info, lastUpdateTime);
    }

    /**
     * The method to create a object record in database by subject.
     *
     * @param subject: subject
     * @param object:  object
     * @param dacMod:  DAC mod
     * @return 0: created failed; >0: created succeed
     */
    public int createObject(ACLSubject subject, ACLObject object, String dacMod) {
        int addObjectResult = dacDatabaseOperation.addObject(object.getId(),
                object.getName(),
                object.getInfo(),
                subject.getId(),
                subject.getName(),
                object.getCreateTime(),
                object.isExecutable());
        int addCapabilityResult = createCapability(subject, subject, object, "orwcd");
        return addCapabilityResult | addObjectResult;
    }

    /**
     * The method to delete a object by id, and remove all capabilities based on subject.
     *
     * @param id: subject id
     * @return 0: delete failed 1: deleted succeed
     */
    public int deleteObject(int id) {
        int deleteObjectResult = dacDatabaseOperation.deleteObject(id);
        int deleteCapabilitiesByObjectIdResult = dacDatabaseOperation.deleteCapabilityByObjectId(id);
        return deleteCapabilitiesByObjectIdResult | deleteObjectResult;
    }

    /**
     * The method to modify object's values includes name, info, lastUpdateTime and executeable
     *
     * @param id:             object's id
     * @param name:           new name
     * @param info:           new info
     * @param lastUpdateTime: last update time
     * @param executable:     executeable
     * @return
     */
    public int modifyObject(int id, String name, String info, long lastUpdateTime, boolean executable) {
        return dacDatabaseOperation.modifyObject(id, name, info, lastUpdateTime, executable);
    }

    /**
     * The method to create a capability record to database.
     *
     * @param grantSubject: subject will grant capability
     * @param subject:      subject will be granted capability
     * @param object:       subject with capability
     * @return 0: created failed 1: created succeed 2:there is a black token 3: granted subject has not control of object
     */
    public int createCapability(ACLSubject grantSubject, ACLSubject subject, ACLObject object, String capabilityString) {
        boolean ifGrantControl = capabilityString.charAt(3) == 'c';
        if (dacDatabaseOperation.queryBlackTokenByObjectIdGrantedSubjectIdSubjectId(object.getId(),
                grantSubject.getId(),
                subject.getId(),
                capabilityString).isBlackToken()) {
            return 2;
        } else {
            if (grantSubject.getId() == object.getCreatedSubjectId()) {
                return createCapabilityDatabaseOperation(grantSubject, subject, object, capabilityString);

            } else {
                ArrayList<Capability> queryCapabilitiesBySubjectIdAndObjectId =
                        dacDatabaseOperation.queryCapabilitiesBySubjectIdAndObjectId(grantSubject.getId(), object.getId());
                boolean ifGrantedSubjectHaveOwnObject = false;
                if (queryCapabilitiesBySubjectIdAndObjectId != null) {
                    for (Capability capability : queryCapabilitiesBySubjectIdAndObjectId
                            ) {
                        ifGrantedSubjectHaveOwnObject = capability.getCapabilityString().charAt(3) == 'c';
                    }
                }
                if (ifGrantedSubjectHaveOwnObject) {
                    return createCapabilityDatabaseOperation(grantSubject, subject, object, capabilityString);
                } else {
                    return 3;
                }

            }
        }
    }

    /**
     * The method to delete capability record according granted subject's id, subject's id, object's id and capability string.
     * And will deleted all capabilities called capability string granted by subject.
     * @param grantedSubjectId: granted subject's id
     * @param subjectId: subject's id
     * @param objectId: object's id
     * @param capabilityString: capability string
     * @return 0: deleted failed >0: deleted succeed
     */
    public int deleteCapability(int grantedSubjectId, int subjectId, int objectId, String capabilityString) {
        int deleteCapabilityResult = dacDatabaseOperation.deleteCapabilityByGSSOCSId(grantedSubjectId,
                subjectId,
                objectId,
                capabilityString);
        dacDatabaseOperation.deleteCapbilityByGrantedSubjectId(subjectId);
        return deleteCapabilityResult;
    }





    private int createCapabilityDatabaseOperation(ACLSubject grantedSubject,
                                                   ACLSubject subject,
                                                   ACLObject object,
                                                   String capabilityString) {
        Capability capability = new Capability(grantedSubject.getId() * 1000000 + subject.getId() * 1000 + object.getId(),
                object.getId(),
                object.getName(),
                subject.getId(),
                subject.getName(),
                subject.getId(),
                subject.getName(),
                new Date().getTime(),
                capabilityString);
        return dacDatabaseOperation.addCapability(capability.getCapabilityId(),
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

    public int createBlackToken(ACLSubject grantedSubject, ACLSubject subject, ACLObject object, String capabilityString) {
        return 0;
    }


}
