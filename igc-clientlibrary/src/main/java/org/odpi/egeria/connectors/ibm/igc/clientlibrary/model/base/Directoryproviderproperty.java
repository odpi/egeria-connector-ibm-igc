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
import org.odpi.egeria.connectors.ibm.igc.clientlibrary.model.common.Reference;
import java.util.Date;

/**
 * POJO for the {@code directoryproviderproperty} asset type in IGC, displayed as '{@literal DirectoryProviderProperty}' in the IGC UI.
 * <br><br>
 * (this code has been created based on out-of-the-box IGC metadata types.
 *  If modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("directoryproviderproperty")
public class Directoryproviderproperty extends Reference {

    @JsonProperty("created_by")
    protected String createdBy;

    @JsonProperty("created_on")
    protected Date createdOn;

    @JsonProperty("modified_by")
    protected String modifiedBy;

    @JsonProperty("modified_on")
    protected Date modifiedOn;

    @JsonProperty("of_directory_provider_configuration")
    protected Directoryproviderconfiguration ofDirectoryProviderConfiguration;

    @JsonProperty("of_provider_property_info")
    protected Providerpropertyinfo ofProviderPropertyInfo;

    @JsonProperty("of_provider_property_type")
    protected Providerpropertytype ofProviderPropertyType;

    @JsonProperty("value")
    protected String value;

    /**
     * Retrieve the {@code created_by} property (displayed as '{@literal Created By}') of the object.
     * @return {@code String}
     */
    @JsonProperty("created_by")
    public String getCreatedBy() { return this.createdBy; }

    /**
     * Set the {@code created_by} property (displayed as {@code Created By}) of the object.
     * @param createdBy the value to set
     */
    @JsonProperty("created_by")
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    /**
     * Retrieve the {@code created_on} property (displayed as '{@literal Created On}') of the object.
     * @return {@code Date}
     */
    @JsonProperty("created_on")
    public Date getCreatedOn() { return this.createdOn; }

    /**
     * Set the {@code created_on} property (displayed as {@code Created On}) of the object.
     * @param createdOn the value to set
     */
    @JsonProperty("created_on")
    public void setCreatedOn(Date createdOn) { this.createdOn = createdOn; }

    /**
     * Retrieve the {@code modified_by} property (displayed as '{@literal Modified By}') of the object.
     * @return {@code String}
     */
    @JsonProperty("modified_by")
    public String getModifiedBy() { return this.modifiedBy; }

    /**
     * Set the {@code modified_by} property (displayed as {@code Modified By}) of the object.
     * @param modifiedBy the value to set
     */
    @JsonProperty("modified_by")
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

    /**
     * Retrieve the {@code modified_on} property (displayed as '{@literal Modified On}') of the object.
     * @return {@code Date}
     */
    @JsonProperty("modified_on")
    public Date getModifiedOn() { return this.modifiedOn; }

    /**
     * Set the {@code modified_on} property (displayed as {@code Modified On}) of the object.
     * @param modifiedOn the value to set
     */
    @JsonProperty("modified_on")
    public void setModifiedOn(Date modifiedOn) { this.modifiedOn = modifiedOn; }

    /**
     * Retrieve the {@code of_directory_provider_configuration} property (displayed as '{@literal Of Directory Provider Configuration}') of the object.
     * @return {@code Directoryproviderconfiguration}
     */
    @JsonProperty("of_directory_provider_configuration")
    public Directoryproviderconfiguration getOfDirectoryProviderConfiguration() { return this.ofDirectoryProviderConfiguration; }

    /**
     * Set the {@code of_directory_provider_configuration} property (displayed as {@code Of Directory Provider Configuration}) of the object.
     * @param ofDirectoryProviderConfiguration the value to set
     */
    @JsonProperty("of_directory_provider_configuration")
    public void setOfDirectoryProviderConfiguration(Directoryproviderconfiguration ofDirectoryProviderConfiguration) { this.ofDirectoryProviderConfiguration = ofDirectoryProviderConfiguration; }

    /**
     * Retrieve the {@code of_provider_property_info} property (displayed as '{@literal Of Provider Property Info}') of the object.
     * @return {@code Providerpropertyinfo}
     */
    @JsonProperty("of_provider_property_info")
    public Providerpropertyinfo getOfProviderPropertyInfo() { return this.ofProviderPropertyInfo; }

    /**
     * Set the {@code of_provider_property_info} property (displayed as {@code Of Provider Property Info}) of the object.
     * @param ofProviderPropertyInfo the value to set
     */
    @JsonProperty("of_provider_property_info")
    public void setOfProviderPropertyInfo(Providerpropertyinfo ofProviderPropertyInfo) { this.ofProviderPropertyInfo = ofProviderPropertyInfo; }

    /**
     * Retrieve the {@code of_provider_property_type} property (displayed as '{@literal Of Provider Property Type}') of the object.
     * @return {@code Providerpropertytype}
     */
    @JsonProperty("of_provider_property_type")
    public Providerpropertytype getOfProviderPropertyType() { return this.ofProviderPropertyType; }

    /**
     * Set the {@code of_provider_property_type} property (displayed as {@code Of Provider Property Type}) of the object.
     * @param ofProviderPropertyType the value to set
     */
    @JsonProperty("of_provider_property_type")
    public void setOfProviderPropertyType(Providerpropertytype ofProviderPropertyType) { this.ofProviderPropertyType = ofProviderPropertyType; }

    /**
     * Retrieve the {@code value} property (displayed as '{@literal Value}') of the object.
     * @return {@code String}
     */
    @JsonProperty("value")
    public String getValue() { return this.value; }

    /**
     * Set the {@code value} property (displayed as {@code Value}) of the object.
     * @param value the value to set
     */
    @JsonProperty("value")
    public void setValue(String value) { this.value = value; }

}
