package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.org.JobTitle;

import javax.persistence.*;

/**
 * User organization many to many linked entity
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "user_org_link")
public class UserOrg {

    @EmbeddedId
    private UserOrgId UserOrgId;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobTitle jobTitle;

    public UserOrgId getUserOrgId() {
        return UserOrgId;
    }

    public void setUserOrgId(UserOrgId userOrgId) {
        UserOrgId = userOrgId;
    }

    public JobTitle getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(JobTitle jobTitle) {
        this.jobTitle = jobTitle;
    }
}
