package com.hexaware.Entity;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int logId;
    
    private String action;

    @ManyToOne
    private Users user;;

    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp timestamp;

    public Audit() {
    }

    public Audit(String action, Users user, Timestamp timestamp) {
        this.action = action;
        this.user = user;
        this.timestamp = timestamp;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

	@Override
	public String toString() {
		return "Audit [logId=" + logId + ", action=" + action + ", user=" + user + ", timestamp=" + timestamp + "]";
	}

}