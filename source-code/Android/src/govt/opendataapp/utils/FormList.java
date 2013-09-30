package govt.opendataapp.utils;

public class FormList {
	String formName;
	int formId;
	String formLocation;

	long downloadTime;

	

	
	public String getFormName() {
		return formName;
	}
	public void setFormName(String formName) {
		this.formName = formName;
	}
	
	public int getFormId() {
		return formId;
	}
	public void setFormId(int formId) {
		this.formId = formId;
	}
	
	public String getFormLocation() {
		return formLocation;
	}
	
	public void setFormLocation(String formLocation) {
		this.formLocation = formLocation;
	}
	
	public long getFormDownloadTime() {
		return downloadTime;
	}
	public void setFormDownloadTime(long downloadTime) {
		this.downloadTime = downloadTime;
	}

}
