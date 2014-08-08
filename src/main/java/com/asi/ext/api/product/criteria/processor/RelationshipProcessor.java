/**
 * 
 */
package com.asi.ext.api.product.criteria.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.asi.service.product.client.vo.CriteriaSetRelationship;
import com.asi.service.product.client.vo.CriteriaSetValuePath;
import com.asi.service.product.client.vo.Relationship;

/**
 * @author Rahul K
 * 
 */
public class RelationshipProcessor {

    protected Relationship createRelationship(String name, String parent, String child, String productId, int relationId) {
        Integer pid = Integer.parseInt(productId);
        Integer parentId = Integer.parseInt(parent);
        Integer childId = Integer.parseInt(child);

        Relationship rel = new Relationship();
        rel.setID(relationId);
        rel.setProductId(pid);
        rel.setParentCriteriaSetId(parentId);
        rel.setName(name);
        List<CriteriaSetRelationship> relationshipGrouperList = new ArrayList<CriteriaSetRelationship>(2);

        CriteriaSetRelationship parentRel = new CriteriaSetRelationship();
        parentRel.setRelationshipId(relationId);
        parentRel.setIsParent(true);
        parentRel.setCriteriaSetId(parentId);
        parentRel.setProductId(pid);

        relationshipGrouperList.add(parentRel);

        CriteriaSetRelationship childRel = new CriteriaSetRelationship();
        childRel.setRelationshipId(relationId);
        childRel.setIsParent(false);
        childRel.setCriteriaSetId(childId);
        childRel.setProductId(pid);

        relationshipGrouperList.add(childRel);
        rel.setCriteriaSetRelationships(relationshipGrouperList);

        return rel;
    }

    protected List<CriteriaSetValuePath> getNewCriteriaSetValuePaths(String parentId, String childId, Integer relationId,
            String pid, Integer valuePathUniqId) {
        Integer parent = Integer.parseInt(parentId);
        Integer child = Integer.parseInt(childId);
        Integer productId = Integer.parseInt(pid);

        List<CriteriaSetValuePath> crtValuePath = new ArrayList<CriteriaSetValuePath>();

        CriteriaSetValuePath parentPath = new CriteriaSetValuePath();
        parentPath.setID(valuePathUniqId);
        parentPath.setIsParent(true);
        parentPath.setCriteriaSetValueId(parent);
        parentPath.setProductId(productId);
        parentPath.setRelationshipId(relationId);

        crtValuePath.add(parentPath);

        CriteriaSetValuePath childPath = new CriteriaSetValuePath();
        childPath.setID(valuePathUniqId);
        childPath.setIsParent(false);
        childPath.setCriteriaSetValueId(child);
        childPath.setProductId(productId);
        childPath.setRelationshipId(relationId);

        crtValuePath.add(childPath);

        return crtValuePath;
    }

    public Relationship getRelationshipBasedCriteriaIds(String parentId, String childId, List<Relationship> extRelations) {
        for (Relationship rel : extRelations) {
            if (isRelationshipMatches(parentId, childId, rel.getCriteriaSetRelationships())) {
                return rel;
            }
        }
        return null;
    }

    private boolean isRelationshipMatches(String parent, String child, List<CriteriaSetRelationship> relationshipGrouperList) {
        boolean parentMatched = false;
        boolean childMatched = false;
        for (CriteriaSetRelationship crtRel : relationshipGrouperList) {
            if (crtRel.getIsParent() && String.valueOf(crtRel.getCriteriaSetId()).equalsIgnoreCase(parent)) {
                parentMatched = true;
            }
            if (!crtRel.getIsParent() && String.valueOf(crtRel.getCriteriaSetId()).equalsIgnoreCase(child)) {
                childMatched = true;
            }
        }
        if (parentMatched && childMatched) {
            return true;
        } else {
            return false;
        }
    }

    public Relationship compareAndUpdateRelationShip(Relationship relToCompare, String parentId, String childId,
            List<Relationship> extRelationships) {

        Relationship tempRelationship = getRelationshipBasedCriteriaIds(parentId, childId, extRelationships);
        if (tempRelationship == null) {
            // There is no combination exists in product, return the new one
            return relToCompare;
        } else {
            tempRelationship.setCriteriaSetValuePaths(compareCriteriaSetValuePaths(relToCompare.getCriteriaSetValuePaths(),
                    tempRelationship.getCriteriaSetValuePaths(), tempRelationship.getID()));
        }

        return tempRelationship;
    }

    // Really complex method
    private List<CriteriaSetValuePath> compareCriteriaSetValuePaths(List<CriteriaSetValuePath> newPaths,
            List<CriteriaSetValuePath> extPaths, Integer relationId) {

        if (extPaths == null || extPaths.isEmpty()) {
            // Set existing relationship Id to all paths
            return syncValuePath(newPaths, relationId);
        } else {
            List<CriteriaSetValuePath> processedItems = new ArrayList<CriteriaSetValuePath>();

            // Core comparison - COMPLEX, need to compare each individual path, like a 4 x 4 matrix compare
            Iterator<CriteriaSetValuePath> newValuePathItr = newPaths.iterator();
            List<Integer> processedIds = new ArrayList<Integer>();
            while (newValuePathItr.hasNext()) {
                CriteriaSetValuePath nValuePathP1 = (CriteriaSetValuePath) newValuePathItr.next();

                if (processedIds.contains(nValuePathP1.getID())) {
                    continue;
                }
                CriteriaSetValuePath nValuePathP2 = getCriteriaSetValuePathById(nValuePathP1.getID(), !nValuePathP1.getIsParent(),
                        newPaths);
                boolean matchFound = false;
                for (CriteriaSetValuePath extValuePath : extPaths) {
                    if (nValuePathP1.getIsParent().equals(extValuePath.getIsParent())
                            && nValuePathP1.getCriteriaSetValueId().equals(extValuePath.getCriteriaSetValueId())) {
                        CriteriaSetValuePath matchingValPath = getCriteriaSetValuePathByIdCriteriaValueId(extValuePath.getID(),
                                nValuePathP2.getCriteriaSetValueId(), nValuePathP2.getIsParent(), extPaths);
                        if (matchingValPath != null) {
                            matchFound = true;
                            processedItems.add(matchingValPath);
                            processedItems.add(extValuePath);
                            break;
                        }

                    }
                    // CriteriaSetValuePath extValuePath2 = get
                }

                if (matchFound) {
                    processedIds.add(nValuePathP1.getID());
                    processedIds.add(nValuePathP2.getID());
                } else {
                    nValuePathP1.setRelationshipId(relationId);
                    nValuePathP2.setRelationshipId(relationId);
                    processedItems.add(nValuePathP1);
                    processedItems.add(nValuePathP2);
                    processedIds.add(nValuePathP1.getID());
                }

            }

            return processedItems;

        }
    }

    public CriteriaSetValuePath getCriteriaSetValuePathById(Integer id, boolean parent, List<CriteriaSetValuePath> valuePaths) {
        for (CriteriaSetValuePath path : valuePaths) {
            if (path.getID().equals(id) && path.getIsParent().equals(parent)) {
                return path;
            }
        }
        return null;
    }

    public CriteriaSetValuePath getCriteriaSetValuePathByIdCriteriaValueId(Integer id, Integer criteriaId, boolean parent,
            List<CriteriaSetValuePath> valuePaths) {
        for (CriteriaSetValuePath path : valuePaths) {
            if (path.getID().equals(id) && path.getIsParent().equals(parent) && criteriaId.equals(path.getCriteriaSetValueId())) {
                return path;
            }
        }
        return null;
    }

    private List<CriteriaSetValuePath> syncValuePath(List<CriteriaSetValuePath> newPaths, Integer relId) {
        List<CriteriaSetValuePath> syncedPaths = new ArrayList<CriteriaSetValuePath>();
        for (CriteriaSetValuePath path : newPaths) {
            path.setRelationshipId(relId);
            syncedPaths.add(path);
        }
        return syncedPaths;
    }

}
