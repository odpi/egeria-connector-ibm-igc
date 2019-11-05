/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.egeria.connectors.ibm.igc.clientlibrary.model.base;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonTypeName;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.egeria.connectors.ibm.igc.clientlibrary.model.common.ItemList;
import java.util.Date;

/**
 * POJO for the {@code xml_schema_library} asset type in IGC, displayed as '{@literal XML Schema Library}' in the IGC UI.
 * <br><br>
 * (this code has been created based on out-of-the-box IGC metadata types.
 *  If modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("xml_schema_library")
public class XmlSchemaLibrary extends InformationAsset {

    @JsonProperty("description")
    protected String description;

    @JsonProperty("id")
    protected String id;

    @JsonProperty("in_collections")
    protected ItemList<Collection> inCollections;

    @JsonProperty("xml_schema_definitions")
    protected ItemList<XmlSchemaDefinition> xmlSchemaDefinitions;

    /**
     * Retrieve the {@code description} property (displayed as '{@literal Description}') of the object.
     * @return {@code String}
     */
    @JsonProperty("description")
    public String getDescription() { return this.description; }

    /**
     * Set the {@code description} property (displayed as {@code Description}) of the object.
     * @param description the value to set
     */
    @JsonProperty("description")
    public void setDescription(String description) { this.description = description; }

    /**
     * Retrieve the {@code id} property (displayed as '{@literal ID}') of the object.
     * @return {@code String}
     */
    @JsonProperty("id")
    public String getTheId() { return this.id; }

    /**
     * Set the {@code id} property (displayed as {@code ID}) of the object.
     * @param id the value to set
     */
    @JsonProperty("id")
    public void setTheId(String id) { this.id = id; }

    /**
     * Retrieve the {@code in_collections} property (displayed as '{@literal In Collections}') of the object.
     * @return {@code ItemList<Collection>}
     */
    @JsonProperty("in_collections")
    public ItemList<Collection> getInCollections() { return this.inCollections; }

    /**
     * Set the {@code in_collections} property (displayed as {@code In Collections}) of the object.
     * @param inCollections the value to set
     */
    @JsonProperty("in_collections")
    public void setInCollections(ItemList<Collection> inCollections) { this.inCollections = inCollections; }

    /**
     * Retrieve the {@code xml_schema_definitions} property (displayed as '{@literal XML Schema Definitions}') of the object.
     * @return {@code ItemList<XmlSchemaDefinition>}
     */
    @JsonProperty("xml_schema_definitions")
    public ItemList<XmlSchemaDefinition> getXmlSchemaDefinitions() { return this.xmlSchemaDefinitions; }

    /**
     * Set the {@code xml_schema_definitions} property (displayed as {@code XML Schema Definitions}) of the object.
     * @param xmlSchemaDefinitions the value to set
     */
    @JsonProperty("xml_schema_definitions")
    public void setXmlSchemaDefinitions(ItemList<XmlSchemaDefinition> xmlSchemaDefinitions) { this.xmlSchemaDefinitions = xmlSchemaDefinitions; }

}
