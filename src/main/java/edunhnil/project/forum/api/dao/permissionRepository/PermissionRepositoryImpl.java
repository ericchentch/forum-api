package edunhnil.project.forum.api.dao.permissionRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import edunhnil.project.forum.api.dao.AbstractMongoRepository;

@Repository
public class PermissionRepositoryImpl extends AbstractMongoRepository implements PermissionRepository {

    @Override
    public Optional<List<Permission>> getPermissions(Map<String, String> allParams, String keySort, int page,
            int pageSize, String sortField) {
        Query query = generateQueryMongoDB(allParams, Permission.class, keySort, sortField, page, pageSize);
        return replaceFind(query, Permission.class);
    }

    @Override
    public Optional<Permission> getPermissionById(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("_id", id);
        Query query = generateQueryMongoDB(params, Permission.class, "", "", 0, 0);
        return replaceFindOne(query, Permission.class);
    }

    @Override
    public void insertAndUpdate(Permission permission) {
        authenticationTemplate.save(permission, "permissions");
    }

    @Override
    public void deletePermission(String id) {
        Map<String, String> params = new HashMap<>();
        params.put("_id", id);
        Query query = generateQueryMongoDB(params, Permission.class, "", "", 0, 0);
        authenticationTemplate.remove(query, Permission.class);
    }

    @Override
    public Optional<List<Permission>> getPermissionByUser(String userId, String featureId) {
        try {
            ObjectId user_id = new ObjectId(userId);
            ObjectId feature_id = new ObjectId(featureId);
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").in(user_id).and("featureId").in(feature_id));
            return replaceFind(query, Permission.class);
        } catch (IllegalArgumentException e) {
            APP_LOGGER.error("wrong type user id");
            return Optional.empty();
        }
    }

    @Override
    public Optional<List<Permission>> getPermissionByUserId(String userId) {
        try {
            ObjectId user_id = new ObjectId(userId);
            Query query = new Query();
            query.addCriteria(Criteria.where("userId").in(user_id));
            return replaceFind(query, Permission.class);
        } catch (IllegalArgumentException e) {
            APP_LOGGER.error("wrong type user id");
            return Optional.empty();
        }
    }

}
