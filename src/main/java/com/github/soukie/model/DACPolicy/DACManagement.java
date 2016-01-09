package com.github.soukie.model.DACPolicy;

import com.github.soukie.model.DACPolicy.objects.ACLObject;
import com.github.soukie.model.DACPolicy.objects.ACLSubject;
import com.github.soukie.model.DACPolicy.objects.Capability;
import com.github.soukie.model.ModelValues;
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
        String capabilityString = dacMod.equalsIgnoreCase(ModelValues.CENTRALIZED_ACL) ? "orwcd" : "orw_d";
        int addObjectResult = dacDatabaseOperation.addObject(object.getId(),
                object.getName(),
                object.getInfo(),
                subject.getId(),
                subject.getName(),
                object.getCreateTime(),
                object.isExecutable());
        int addCapabilityResult = createCapability(subject, subject, object, dacMod, capabilityString);
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
     * @param dacMod:       dac capability mod
     * @return 0: created failed 1: created succeed.
     */
    public int createCapability(ACLSubject grantSubject, ACLSubject subject, ACLObject object, String dacMod, String capabilityString) {
        int dacModInt = dacMod.equalsIgnoreCase(ModelValues.CENTRALIZED_ACL) ? 1 :
                (dacMod.equalsIgnoreCase(ModelValues.DISTRIBUTED_ACL) ? 2 : 3);
        if (grantSubject.getId() == object.getCreatedSubjectId()) {
            Capability capability = new Capability(grantSubject.getId() * 1000000 + subject.getId() * 1000 + object.getId() + dacModInt,
                    object.getId(),
                    object.getName(),
                    subject.getId(),
                    subject.getName(),
                    subject.getId(),
                    subject.getName(),
                    dacMod,
                    new Date().getTime(),
                    capabilityString);
            return dacDatabaseOperation.addCapability(capability.getCapabilityId(),
                    capability.getObjectId(),
                    capability.getObjectName(),
                    capability.getGrantedSubjectId(),
                    capability.getGrantedSubjectName(),
                    capability.getSubjectId(),
                    capability.getSubjectName(),
                    capability.getCapabilityType(),
                    capability.getCreatedTime(),
                    capability.getLastUpdateTime(),
                    capability.getCapabilityString(),
                    capability.getCapabilityInfo());
        } else {
            ArrayList<Capability> queryCapabilitiesBySubjectIdAndObjectId =
                    dacDatabaseOperation.queryCapabilitiesBySubjectIdAndObjectId(grantSubject.getId(), object.getId());
            boolean ifGrantedSubjectHaveOwnObject = false;
            if (queryCapabilitiesBySubjectIdAndObjectId != null) {
                for (Capability capability : queryCapabilitiesBySubjectIdAndObjectId
                        ) {
                    ifGrantedSubjectHaveOwnObject = capability.getCapabilityString().charAt(3) == 'c' &&
                            capability.getCapabilityType().equalsIgnoreCase(dacMod);
                }
            }
            if (ifGrantedSubjectHaveOwnObject) {
                Capability capability = new Capability(grantSubject.getId() * 1000000 + subject.getId() * 1000 + object.getId() + dacModInt,
                        object.getId(),
                        object.getName(),
                        subject.getId(),
                        subject.getName(),
                        subject.getId(),
                        subject.getName(),
                        dacMod,
                        new Date().getTime(),
                        capabilityString);
                return dacDatabaseOperation.addCapability(capability.getCapabilityId(),
                        capability.getObjectId(),
                        capability.getObjectName(),
                        capability.getGrantedSubjectId(),
                        capability.getGrantedSubjectName(),
                        capability.getSubjectId(),
                        capability.getSubjectName(),
                        capability.getCapabilityType(),
                        capability.getCreatedTime(),
                        capability.getLastUpdateTime(),
                        capability.getCapabilityString(),
                        capability.getCapabilityInfo());
            } else {
                return 0;
            }

        }

    }


}
