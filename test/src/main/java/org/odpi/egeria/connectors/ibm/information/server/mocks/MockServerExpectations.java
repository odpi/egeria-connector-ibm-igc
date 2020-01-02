/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.ibm.information.server.mocks;

import org.mockserver.client.MockServerClient;
import org.mockserver.client.initialize.ExpectationInitializer;
import org.mockserver.matchers.MatchType;
import org.mockserver.matchers.Times;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.model.JsonBody.json;
import static org.mockserver.model.Parameter.param;
import static org.odpi.egeria.connectors.ibm.information.server.mocks.MockConstants.*;

/**
 * Setup a mock server to act as an IGC REST API endpoint against which we can do some thorough testing.
 */
public class MockServerExpectations implements ExpectationInitializer {

    private static final Logger log = LoggerFactory.getLogger(MockServerExpectations.class);

    /**
     * Setup the expectations we will need to respond to various tests.
     * @param mockServerClient the client against which to set the expectations
     */
    public void initializeExpectations(MockServerClient mockServerClient) {

        initializeTypeDetails(mockServerClient);
        initializeIGCClientExpectations(mockServerClient);
        initializeExampleInstances(mockServerClient);
        initializeIGCConnectorExpectations(mockServerClient);
        initializeDataStageConnectorExpectations(mockServerClient);

        setDefaultSearchResponseToNoResults(mockServerClient);

    }

    private void initializeTypeDetails(MockServerClient mockServerClient) {

        setTypesQuery(mockServerClient);
        Resource[] typeFiles = getFilesMatchingPattern("types/*.json");
        if (typeFiles != null) {
            for (Resource typeFile : typeFiles) {
                setTypeDetails(mockServerClient, typeFile.getFilename());
            }
        }

    }

    private void initializeIGCClientExpectations(MockServerClient mockServerClient) {

        setStartupQuery(mockServerClient);
        setTypesQuery(mockServerClient);
        setBundlesQuery(mockServerClient);

        String glossaryIgcType = "category";
        setExamplePartAsset(mockServerClient, glossaryIgcType, MockConstants.GLOSSARY_RID);
        setExampleAssetWithModDetails(mockServerClient, glossaryIgcType, MockConstants.GLOSSARY_RID);

        setLogout(mockServerClient);

    }

    private void initializeExampleInstances(MockServerClient mockServerClient) {

        Resource[] instanceExamples = getFilesMatchingPattern("by_rid/**/*.json");
        if (instanceExamples != null) {
            for (Resource instanceExample : instanceExamples) {
                setDetailsByRidQuery(mockServerClient, instanceExample);
            }
        }

        Resource[] childExamples = getFilesMatchingPattern("by_child_rid/**/*.json");
        if (childExamples != null) {
            for (Resource relationshipExample : childExamples) {
                setDetailsByChildRidQuery(mockServerClient, relationshipExample);
            }
        }

        Resource[] parentExamples = getFilesMatchingPattern("by_parent_rid/**/*.json");
        if (parentExamples != null) {
            for (Resource relationshipExample : parentExamples) {
                setDetailsByParentRidQuery(mockServerClient, relationshipExample);
            }
        }

        Resource[] referenceExamples = getFilesMatchingPattern("ref_by_rid/*.json");
        if (referenceExamples != null) {
            for (Resource referenceExample : referenceExamples) {
                setReferenceByRidQuery(mockServerClient, referenceExample);
            }
        }

        Resource[] fullExamples = getFilesMatchingPattern("full_by_rid/*.json");
        if (fullExamples != null) {
            for (Resource fullExample : fullExamples) {
                setFullByRidQuery(mockServerClient, fullExample);
            }
        }

    }

    private void initializeIGCConnectorExpectations(MockServerClient mockServerClient) {

        setUploadBundle(mockServerClient);

        // Setup responses for specific test cases

        // Glossary tests
        setGlossaryFindByPropertyValue(mockServerClient);
        setGlossaryRelationships(mockServerClient);

        // GlossaryCategory tests
        setGlossaryCategoryFindByPropertyValue(mockServerClient);
        setGlossaryCategoryRelationships(mockServerClient);

        // GlossaryTerm search tests
        setGlossaryTermFindByPropertyValue(mockServerClient);
        setGlossaryTermFindByProperty_displayName(mockServerClient);
        setGlossaryTermFindByProperties_ANY(mockServerClient);
        setGlossaryTermFindByProperties_ALL(mockServerClient);

        // Supertype search tests (can skip folder and schema as they return no results, so our catch-all will handle)
        setAssetFindByPropertyValue(mockServerClient);

        // All types search tests (can skip many as they will return no results, so our catch-all will handle)
        setAllTypesFindByPropertyValue(mockServerClient);

        // Limit by classification tests (can skip many as they will return no results)
        setAllTypesFindByPropertyValue_limitToConfidentiality(mockServerClient);
        setGlossaryTermFindByClassification(mockServerClient);

        // Relationship tests
        setGlossaryTermRelationships(mockServerClient);
        setDatabaseRelationships(mockServerClient);
        setConnectionRelationships(mockServerClient);
        setEndpointRelationships(mockServerClient);
        setDeployedDatabaseSchemaRelationships(mockServerClient);
        setRelationalDBSchemaTypeRelationships(mockServerClient);
        setRelationalTableRelationships(mockServerClient);
        setRelationalColumnRelationships(mockServerClient);
        setDataClassRelationships(mockServerClient);
        setConnectionFSRelationships(mockServerClient);
        setDataFileFolderRelationships(mockServerClient);
        setDataFileRelationships(mockServerClient);
        setTabularSchemaTypeRelationships(mockServerClient);
        setTabularColumnRelationships(mockServerClient);

    }

    private void initializeDataStageConnectorExpectations(MockServerClient mockServerClient) {

        setJobSyncRuleQueryEmpty(mockServerClient);
        setJobSyncRuleQueryFull(mockServerClient);

        setJobChangeQuery(mockServerClient);

    }

    private void setStartupQuery(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(searchRequest("{\"types\":[\"category\",\"term\",\"information_governance_policy\",\"information_governance_rule\"],\"pageSize\":1,\"workflowMode\":\"draft\"}"))
                .respond(withResponse(getResourceFileContents("no_results.json")));
    }

    private void setTypesQuery(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(typesRequest())
                .respond(withResponse(getResourceFileContents("types.json")));
    }

    private void setBundlesQuery(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(bundlesRequest())
                .respond(withResponse("[\"OMRS\"]"));
    }

    private void setTypeDetails(MockServerClient mockServerClient, String typeFilename) {
        String typeName = typeFilename.substring(0, typeFilename.indexOf(".json"));
        mockServerClient
                .withSecure(true)
                .when(typesRequest(typeName)
                                .withQueryStringParameters(
                                        param("showViewProperties", "true"),
                                        param("showCreateProperties", "true"),
                                        param("showEditProperties", "true")
                                )
                )
                .respond(withResponse(getResourceFileContents("types" + File.separator + typeName + ".json")));
    }

    private void setExamplePartAsset(MockServerClient mockServerClient, String type, String rid) {
        mockServerClient
                .withSecure(true)
                .when(searchRequest("{\"types\":[\"" + type + "\"],\"properties\":[\"short_description\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"_id\",\"operator\":\"=\",\"value\":\"" + rid + "\"}],\"operator\":\"and\"}}"))
                .respond(withResponse(getResourceFileContents("rid_part_" + rid + ".json")));
    }

    private void setExampleAssetWithModDetails(MockServerClient mockServerClient, String type, String rid) {
        mockServerClient
                .withSecure(true)
                .when(searchRequest("{\"types\":[\"" + type + "\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":2,\"where\":{\"conditions\":[{\"property\":\"_id\",\"operator\":\"=\",\"value\":\"" + rid + "\"}],\"operator\":\"and\"}}"))
                .respond(withResponse(getResourceFileContents("rid_mod_" + rid + ".json")));
    }

    private void setLogout(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(logoutRequest())
                .respond(response().withStatusCode(200));
    }

    private void setUploadBundle(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(upsertBundleRequest())
                .respond(response().withStatusCode(200));
    }

    private void setJobSyncRuleQueryEmpty(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(jobSyncRuleRequest(), Times.exactly(1))
                .respond(withResponse(getResourceFileContents("no_results.json")));
    }

    private void setJobSyncRuleQueryFull(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(jobSyncRuleRequest())
                .respond(withResponse(getResourceFileContents("job_sync_rule.json")));
    }

    private void setJobChangeQuery(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(searchRequest(
                        json(
                                "{\"types\":[\"dsjob\"],\"properties\":[\"short_description\",\"long_description\",\"references_local_or_shared_containers\",\"type\",\"reads_from_(design)\",\"writes_to_(design)\",\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"modified_on\",\"operator\":\"<=\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        )))
                .respond(withResponse(getResourceFileContents("changed_jobs.json")));
    }

    private void setDetailsByRidQuery(MockServerClient mockServerClient, Resource resource) {
        URL url = null;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            log.error("Unable to retrieve detailed file from: {}", resource, e);
        }
        if (url != null) {
            String filename = url.getFile();
            String rid = getRidFromFilename(filename);
            String type = getTypeFromFilename(filename);
            mockServerClient
                    .withSecure(true)
                    .when(searchRequest(
                            json(
                                    "{\"types\":[\"" + type + "\"],\"where\":{\"conditions\":[{\"property\":\"_id\",\"operator\":\"=\",\"value\":\"" + rid + "\"}]}}",
                                    MatchType.ONLY_MATCHING_FIELDS
                            )))
                    .respond(withResponse(getResourceFileContents("by_rid" + File.separator + type + File.separator + rid + ".json")));
        }
    }

    private void setDetailsByChildRidQuery(MockServerClient mockServerClient, Resource resource) {
        URL url = null;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            log.error("Unable to retrieve detailed file from: {}", resource, e);
        }
        if (url != null) {
            String filename = url.getFile();
            String rid = getRidFromFilename(filename);
            String type = getTypeFromFilename(filename);
            String property = null;
            switch (type) {
                case "database_schema":
                    property = "database_tables";
                    break;
            }
            if (property != null) {
                mockServerClient
                        .withSecure(true)
                        .when(searchRequest(
                                json(
                                        "{\"types\":[\"" + type + "\"],\"where\":{\"conditions\":[{\"property\":\"" + property + "\",\"operator\":\"=\",\"value\":\"" + rid + "\"}]}}",
                                        MatchType.ONLY_MATCHING_FIELDS
                                )))
                        .respond(withResponse(getResourceFileContents("by_child_rid" + File.separator + type + File.separator + rid + ".json")));
            }
        }
    }

    private void setDetailsByParentRidQuery(MockServerClient mockServerClient, Resource resource) {
        URL url = null;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            log.error("Unable to retrieve detailed file from: {}", resource, e);
        }
        if (url != null) {
            String filename = url.getFile();
            String rid = getRidFromFilename(filename);
            String type = getTypeFromFilename(filename);
            String property = null;
            switch (type) {
                case "database_column":
                    property = "database_table_or_view";
                    break;
                case "data_file_field":
                    property = "data_file_record";
                    break;
                case "stage_column":
                case "ds_stage_column":
                    property = "link.job_or_container";
                    break;
                case "link":
                case "stage":
                    property = "job_or_container";
                    break;
            }
            if (property != null) {
                mockServerClient
                        .withSecure(true)
                        .when(searchRequest(
                                json(
                                        "{\"types\":[\"" + type + "\"],\"where\":{\"conditions\":[{\"property\":\"" + property + "\",\"operator\":\"=\",\"value\":\"" + rid + "\"}]}}",
                                        MatchType.ONLY_MATCHING_FIELDS
                                )))
                        .respond(withResponse(getResourceFileContents("by_parent_rid" + File.separator + type + File.separator + rid + ".json")));
            }
        }
    }

    private void setReferenceByRidQuery(MockServerClient mockServerClient, Resource resource) {
        URL url = null;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            log.error("Unable to retrieve reference file from: {}", resource, e);
        }
        if (url != null) {
            String filename = url.getFile();
            String rid = getRidFromFilename(filename);
            mockServerClient
                    .withSecure(true)
                    .when(searchRequest(
                            json(
                                    "{\"types\":[\"main_object\",\"classification\",\"label\",\"user\",\"group\"],\"where\":{\"conditions\":[{\"property\":\"_id\",\"operator\":\"=\",\"value\":\"" + rid + "\"}]}}",
                                    MatchType.ONLY_MATCHING_FIELDS
                            )))
                    .respond(withResponse(getResourceFileContents("ref_by_rid" + File.separator + rid + ".json")));
        }
    }

    private void setFullByRidQuery(MockServerClient mockServerClient, Resource resource) {
        URL url = null;
        try {
            url = resource.getURL();
        } catch (IOException e) {
            log.error("Unable to retrieve full details file from: {}", resource, e);
        }
        if (url != null) {
            String filename = url.getFile();
            String rid = getRidFromFilename(filename);
            mockServerClient
                    .withSecure(true)
                    .when(assetByRidRequest(rid))
                    .respond(withResponse(getResourceFileContents("full_by_rid" + File.separator + rid + ".json")));
        }
    }

    private String getRidFromFilename(String filename) {
        return filename.substring(filename.lastIndexOf("/") + 1, filename.indexOf(".json"));
    }

    private String getTypeFromFilename(String filename) {
        String path = filename.substring(filename.indexOf("/") + 1, filename.lastIndexOf("/"));
        return path.substring(path.lastIndexOf("/") + 1);
    }

    private void setDefaultSearchResponseToNoResults(MockServerClient mockServerClient) {
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest())
                .respond(withResponse(getResourceFileContents("no_results.json")));
    }

    private void setGlossaryFindByPropertyValue(MockServerClient mockServerClient) {
        String caseName = "GlossaryFindByPropertyValue";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"category\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"parent_category\",\"operator\":\"isNull\",\"negated\":false},{\"property\":\"name\",\"operator\":\"<>\",\"value\":\"Classifications\"}],\"operator\":\"and\"},{\"conditions\":[{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"a\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"a\"},{\"property\":\"long_description\",\"operator\":\"like %{0}%\",\"value\":\"a\"},{\"property\":\"language\",\"operator\":\"like %{0}%\",\"value\":\"a\"}],\"operator\":\"or\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results.json")));
    }

    private void setGlossaryRelationships(MockServerClient mockServerClient) {
        String caseName = "GlossaryRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"category\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"category_path\",\"operator\":\"=\",\"value\":\"" + GLOSSARY_RID + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "categories.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"term\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"parent_category.category_path\",\"operator\":\"=\",\"value\":\"" + GLOSSARY_RID + "\"},{\"property\":\"parent_category\",\"operator\":\"=\",\"value\":\"" + GLOSSARY_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "terms.json")));
    }

    private void setGlossaryCategoryFindByPropertyValue(MockServerClient mockServerClient) {
        String caseName = "GlossaryCategoryFindByPropertyValue";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"category\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"parent_category\",\"operator\":\"isNull\",\"negated\":true},{\"property\":\"category_path.name\",\"operator\":\"<>\",\"value\":\"Classifications\"}],\"operator\":\"and\"},{\"conditions\":[{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"e\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"e\"}],\"operator\":\"or\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results.json")));
    }

    private void setGlossaryCategoryRelationships(MockServerClient mockServerClient) {
        String caseName = "GlossaryCategoryRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"category\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"subcategories\",\"operator\":\"=\",\"value\":\"" + CATEGORY_RID + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "parent_category.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"term\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"parent_category\",\"operator\":\"=\",\"value\":\"" + CATEGORY_RID + "\"},{\"property\":\"referencing_categories\",\"operator\":\"=\",\"value\":\"" + CATEGORY_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "terms.json")));
    }

    private void setGlossaryTermFindByPropertyValue(MockServerClient mockServerClient) {
        String caseName = "GlossaryTermFindByPropertyValue";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"term\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"category_path.name\",\"operator\":\"<>\",\"value\":\"Classifications\"}],\"operator\":\"and\"},{\"conditions\":[{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"long_description\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"abbreviation\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"example\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"usage\",\"operator\":\"like %{0}%\",\"value\":\"Address\"}],\"operator\":\"or\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results.json")));
    }

    private void setGlossaryTermFindByProperty_displayName(MockServerClient mockServerClient) {
        String caseName = "GlossaryTermFindByProperty_displayName";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"term\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"category_path.name\",\"operator\":\"<>\",\"value\":\"Classifications\"}],\"operator\":\"and\"},{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"Address\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results.json")));
    }

    private void setGlossaryTermFindByProperties_ANY(MockServerClient mockServerClient) {
        String caseName = "GlossaryTermFindByProperties_ANY";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"term\"],\"where\":{\"conditions\":[{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"Number\"},{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"Address\"}],\"operator\":\"or\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results.json")));
    }

    private void setGlossaryTermFindByProperties_ALL(MockServerClient mockServerClient) {
        String caseName = "GlossaryTermFindByProperties_ALL";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"term\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"category_path.name\",\"operator\":\"<>\",\"value\":\"Classifications\"}],\"operator\":\"and\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"number\"},{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"Address\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results.json")));
    }

    private void setAssetFindByPropertyValue(MockServerClient mockServerClient) {
        String caseName = "AssetFindByPropertyValue";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"data_file\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"},{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"}],\"operator\":\"or\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results_data_file.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"database\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"},{\"property\":\"dbms\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"},{\"property\":\"dbms_server_instance\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"},{\"property\":\"dbms_version\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"},{\"property\":\"imported_from\",\"operator\":\"like %{0}%\",\"value\":\"COMPDIR\"}],\"operator\":\"or\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results_database.json")));
    }

    private void setAllTypesFindByPropertyValue(MockServerClient mockServerClient) {
        String caseName = "AllTypesFindByPropertyValue";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"term\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"category_path.name\",\"operator\":\"<>\",\"value\":\"Classifications\"}],\"operator\":\"and\"},{\"conditions\":[{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"long_description\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"abbreviation\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"example\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"usage\",\"operator\":\"like %{0}%\",\"value\":\"Address\"}],\"operator\":\"or\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results_term.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"data_class\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"example\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"class_code\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"expression\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"provider\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"data_class_type_single\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"valid_value_strings\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"data_type_filter_elements_enum\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"validValueReferenceFile\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"java_class_name_single\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"regular_expression_single\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"script\",\"operator\":\"like %{0}%\",\"value\":\"Address\"}],\"operator\":\"or\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results_data_class.json")));
    }

    private void setAllTypesFindByPropertyValue_limitToConfidentiality(MockServerClient mockServerClient) {
        String caseName = "AllTypesFindByPropertyValue_limitToConfidentiality";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"term\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"category_path.name\",\"operator\":\"<>\",\"value\":\"Classifications\"}],\"operator\":\"and\"},{\"conditions\":[{\"property\":\"name\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"short_description\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"long_description\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"abbreviation\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"example\",\"operator\":\"like %{0}%\",\"value\":\"Address\"},{\"property\":\"usage\",\"operator\":\"like %{0}%\",\"value\":\"Address\"}],\"operator\":\"or\"},{\"conditions\":[{\"conditions\":[{\"property\":\"assigned_to_terms.parent_category.name\",\"operator\":\"=\",\"value\":\"Confidentiality\"}],\"operator\":\"and\"}],\"operator\":\"and\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results.json")));
    }

    private void setGlossaryTermFindByClassification(MockServerClient mockServerClient) {
        String caseName = "GlossaryTermFindByClassification";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        json(
                                "{\"types\":[\"term\"],\"where\":{\"conditions\":[{\"conditions\":[{\"property\":\"category_path.name\",\"operator\":\"<>\",\"value\":\"Classifications\"}],\"operator\":\"and\"},{\"conditions\":[{\"property\":\"assigned_to_terms.parent_category.name\",\"operator\":\"=\",\"value\":\"Confidentiality\"},{\"property\":\"assigned_to_terms.name\",\"operator\":\"like {0}%\",\"value\":\"3 \"}],\"operator\":\"and\"}],\"operator\":\"and\"}}",
                                MatchType.ONLY_MATCHING_FIELDS
                        ))
                )
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "results.json")));
    }

    private void setGlossaryTermRelationships(MockServerClient mockServerClient) {
        String caseName = "GlossaryTermRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"main_object\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"assigned_to_terms\",\"operator\":\"=\",\"value\":\"" + TERM_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "assigned_assets.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"category\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"terms\",\"operator\":\"=\",\"value\":\"" + TERM_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "parent_category.json")));
    }

    private void setDatabaseRelationships(MockServerClient mockServerClient) {
        String caseName = "DatabaseRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"database_schema\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"database\",\"operator\":\"=\",\"value\":\"" + DATABASE_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "database_schema.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_connection\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"imports_database\",\"operator\":\"=\",\"value\":\"" + DATABASE_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_connection.json")));
    }

    private void setConnectionRelationships(MockServerClient mockServerClient) {
        String caseName = "ConnectionRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"database\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_connections\",\"operator\":\"=\",\"value\":\"" + DATA_CONNECTION_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "database.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"connector\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_connections\",\"operator\":\"=\",\"value\":\"" + DATA_CONNECTION_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "connector.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"connector\"],\"properties\":[\"host\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_connections\",\"operator\":\"=\",\"value\":\"" + DATA_CONNECTION_RID + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "host.json")));
    }

    private void setEndpointRelationships(MockServerClient mockServerClient) {
        String caseName = "EndpointRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_connection\"],\"properties\":[\"name\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_connectors.host\",\"operator\":\"=\",\"value\":\"" + HOST_RID + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_connection.json")));
    }

    private void setDeployedDatabaseSchemaRelationships(MockServerClient mockServerClient) {
        String caseName = "DeployedDatabaseSchemaRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"database\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"database_schemas\",\"operator\":\"=\",\"value\":\"" + DATABASE_SCHEMA_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "database.json")));
    }

    private void setRelationalDBSchemaTypeRelationships(MockServerClient mockServerClient) {
        String caseName = "RelationalDBSchemaTypeRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"database_table\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"database_schema\",\"operator\":\"=\",\"value\":\"" + DATABASE_SCHEMA_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "database_table.json")));
    }

    private void setRelationalTableRelationships(MockServerClient mockServerClient) {
        String caseName = "RelationalTableRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"database_schema\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"database_tables\",\"operator\":\"=\",\"value\":\"" + DATABASE_TABLE_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "database_schema.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"database_column\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"database_table_or_view\",\"operator\":\"=\",\"value\":\"" + DATABASE_TABLE_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "database_column.json")));
    }

    private void setRelationalColumnRelationships(MockServerClient mockServerClient) {
        String caseName = "RelationalColumnRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"term\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"assigned_assets\",\"operator\":\"=\",\"value\":\"" + DATABASE_COLUMN_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "term.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"classification\"],\"properties\":[\"data_class\",\"confidencePercent\",\"threshold\",\"value_frequency\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"classifies_asset\",\"operator\":\"=\",\"value\":\"" + DATABASE_COLUMN_RID + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "classification.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"database_table\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"database_columns\",\"operator\":\"=\",\"value\":\"" + DATABASE_COLUMN_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "database_table.json")));
    }

    private void setDataClassRelationships(MockServerClient mockServerClient) {
        String caseName = "DataClassRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"classification\"],\"properties\":[\"classifies_asset\",\"confidencePercent\",\"threshold\",\"value_frequency\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_class\",\"operator\":\"=\",\"value\":\"" + DATA_CLASS_RID + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "classification.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"amazon_s3_data_file_field\",\"data_file_field\",\"database_column\"],\"properties\":[\"selected_classification\",\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"selected_classification\",\"operator\":\"=\",\"value\":\"" + DATA_CLASS_RID + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_item.json")));
    }

    private void setConnectionFSRelationships(MockServerClient mockServerClient) {
        String caseName = "ConnectionFSRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_file_folder\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_connection\",\"operator\":\"=\",\"value\":\"" + DATA_CONNECTION_RID_FS + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_file_folder.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"connector\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_connections\",\"operator\":\"=\",\"value\":\"" + DATA_CONNECTION_RID_FS + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "connector.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"connector\"],\"properties\":[\"host\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_connections\",\"operator\":\"=\",\"value\":\"" + DATA_CONNECTION_RID_FS + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "host.json")));
    }

    private void setDataFileFolderRelationships(MockServerClient mockServerClient) {
        String caseName = "DataFileFolderRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_file_folder\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_file_folders\",\"operator\":\"=\",\"value\":\"" + DATA_FILE_FOLDER_RID + "\"}],\"operator\":\"and\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_file_folder.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_file\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"parent_folder\",\"operator\":\"=\",\"value\":\"" + DATA_FILE_FOLDER_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_file.json")));
    }

    private void setDataFileRelationships(MockServerClient mockServerClient) {
        String caseName = "DataFileRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_file_record\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_file\",\"operator\":\"=\",\"value\":\"" + DATA_FILE_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_file_record.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_file_folder\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_files\",\"operator\":\"=\",\"value\":\"" + DATA_FILE_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_file_folder.json")));
    }

    private void setTabularSchemaTypeRelationships(MockServerClient mockServerClient) {
        String caseName = "TabularSchemaTypeRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_file\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_file_records\",\"operator\":\"=\",\"value\":\"" + DATA_FILE_RECORD_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_file.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_file_field\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_file_record\",\"operator\":\"=\",\"value\":\"" + DATA_FILE_RECORD_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_file_field.json")));
    }

    private void setTabularColumnRelationships(MockServerClient mockServerClient) {
        String caseName = "TabularColumnRelationships";
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"term\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"assigned_assets\",\"operator\":\"=\",\"value\":\"" + DATA_FILE_FIELD_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "term.json")));
        mockServerClient
                .withSecure(true)
                .when(MockConstants.searchRequest(
                        "{\"types\":[\"data_file_record\"],\"properties\":[\"created_by\",\"created_on\",\"modified_by\",\"modified_on\"],\"pageSize\":100,\"where\":{\"conditions\":[{\"property\":\"data_file_fields\",\"operator\":\"=\",\"value\":\"" + DATA_FILE_FIELD_RID + "\"}],\"operator\":\"or\"}}"
                ))
                .respond(withResponse(getResourceFileContents("by_case" + File.separator + caseName + File.separator + "data_file_record.json")));
    }

    /**
     * Retrieve the contents of a test resource file.
     * @return String
     */
    private String getResourceFileContents(String filename) {

        ClassPathResource resource = new ClassPathResource(filename);
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            log.error("Unable to read resource file: {}", filename, e);
        }
        if (reader != null) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
        return null;

    }

    /**
     * Retrieve the set of resources that match the specified pattern.
     * @param pattern to match for retrieving resources
     * @return {@code Resource[]}
     */
    private Resource[] getFilesMatchingPattern(String pattern) {

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            return resolver.getResources(pattern);
        } catch(IOException e) {
            log.error("Unable to find any matches to pattern: {}", pattern, e);
        }
        return null;

    }

}