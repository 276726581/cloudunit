package fr.treeptik.cloudunit.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.treeptik.cloudunit.model.action.ModuleAction;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.PostLoad;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Entity
public class  Module extends Container implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * linkAlias - userName - password - phpMyAdmin - database
	 */
	@ElementCollection
	protected Map<String, String> moduleInfos = new HashMap<>();

	public Module() {
		this.image = new Image();
		this.moduleInfos = new HashMap<>();
	}

	@Transient
	@JsonIgnore
	private ModuleAction moduleAction;

	private String managerLocation;

	@Transient
	protected String suffixCU;

	public String getManagerLocation() {
		return managerLocation;
	}

	public void setManagerLocation(String managerLocation) {
		this.managerLocation = managerLocation;
	}

	public Map<String, String> getModuleInfos() {
		return moduleInfos;
	}

	public void setModuleInfos(Map<String, String> moduleInfos) {
		this.moduleInfos = moduleInfos;
	}

	public ModuleAction getModuleAction() {
		return moduleAction;
	}

	@PostLoad
	public void initModuleActionFromJPA() {
		ModuleFactory.updateModule(this);
	}

	public void setModuleAction(ModuleAction moduleAction) {
		this.moduleAction = moduleAction;
	}

	/**
	 * Méthode permettant de savoir si oui ou non
	 * c'est un tool plutot qu'un module fonctionnel
	 */
	@JsonIgnore
	public boolean isTool() {
		if (name != null &&
				(name.contains("git"))
				|| name.contains("maven")) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "Module [id=" + id + ", startDate=" + startDate + ", name="
				+ name + ", cloudId=" + containerID + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Module other = (Module) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@JsonIgnore
	public Long getInstanceNumber() {
		if (name == null) {
			throw new RuntimeException(
					"Cannot get instance number without first call initNewModule");
		}
		return Long.parseLong((name.substring(name.lastIndexOf("-") + 1)));
	}

}
