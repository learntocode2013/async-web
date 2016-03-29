package org.dibakar.rest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Developer")
public class Engineer {
    @XmlElement(name = "name")
    @NotNull(message = "{name.not.valid}")
    private  String _name ;

    @XmlElement(name = "country")
    @NotNull(message = "{country.not.valid}")
    private  String _country ;

    @XmlElement(name = "industryExperience")
    @Min(value = 1,message = "{industry.experience.not.valid}")
    private  int _experience ;

    public Engineer() {
    }

    public Engineer(String _name, String _country, int _experience) {
        this._country = _country;
        this._name = _name;
        this._experience = _experience;
    }
}
