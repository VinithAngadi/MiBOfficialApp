package makeinbvb.com.mibofficialapp;

public class UserData
{
    String userName;
    String password;
    String emailID;
    String phoneNumber;
    String userType;
    String collegeName;
    String urn;
    String degree;
    String specialization;
    String pupa_reg;
    String esummit_reg;
    String intel_ideation_reg;
    String butterfly_reg;

    public UserData()
    {

    }

    public UserData(String userName, String password, String emailID, String phoneNumber,String userType, String collegeName, String urn, String degree, String specialization, String pupa_reg, String esummit_reg, String intel_ideation_reg, String butterfly_reg)
    {
        this.userName = userName;
        this.password = password;
        this.emailID = emailID;
        this.phoneNumber = phoneNumber;
        this.userType = userType;
        this.collegeName = collegeName;
        this.urn = urn;
        this.degree = degree;
        this.specialization = specialization;
        this.pupa_reg = pupa_reg;
        this.esummit_reg = esummit_reg;
        this.butterfly_reg = butterfly_reg;
        this.intel_ideation_reg = intel_ideation_reg;
    }
}
