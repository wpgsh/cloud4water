package net.wapwag.wemp.dao.model.link;

import net.wapwag.wemp.dao.model.org.JobInfo;

import javax.persistence.*;

/**
 * User organization many to many linked entity
 * Created by Administrator on 2016/10/25 0025.
 */
@Entity
@Table(name = "user_org_link")
public class UserOrg {

    @EmbeddedId
    private UserOrgId userOrgId;

    @ManyToOne
    @JoinColumn(name = "job_id")
    private JobInfo jobInfo;

    public UserOrgId getUserOrgId() {
        return userOrgId;
    }

    public void setUserOrgId(UserOrgId userOrgId) {
        this.userOrgId = userOrgId;
    }

    public JobInfo getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(JobInfo jobInfo) {
        this.jobInfo = jobInfo;
    }
}
