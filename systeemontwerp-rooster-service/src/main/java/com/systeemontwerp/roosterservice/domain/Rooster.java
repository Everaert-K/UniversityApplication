package com.systeemontwerp.roosterservice.domain;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Document // MongoDB
@CompoundIndex(def = "{'lokaalId': 1, 'date': 1, 'time': 1}", unique = true)
public class Rooster {
	@Id
	private String id;
	private Time time;
	private LocalDate date;
	private String vakId;
	private String lokaalId;
	
	public Rooster() { }

	public Rooster(String vakId, String lokaalId, Time time, LocalDate date) {
		this.vakId = vakId;
		this.lokaalId = lokaalId;
		this.time = time;
		this.date = date;
	}
	

	public String getVakId() {
		return vakId;
	}

	public void setVakId(String vakId) {
		this.vakId = vakId;
	}

	public String getLokaalId() {
		return lokaalId;
	}

	public void setLokaalId(String lokaalId) {
		this.lokaalId = lokaalId;
	}

	public Time getTime() {
		return time;
	}

	public void setTime(Time time) {
		this.time = time;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getId() {
		return id;
	}

	@Override
	public String toString() {
		return "Rooster [id=" + id + ", time=" + time + ", date=" + date + ", vakId=" + vakId + ", lokaalId=" + lokaalId
				+ "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((lokaalId == null) ? 0 : lokaalId.hashCode());
		result = prime * result + ((time == null) ? 0 : time.hashCode());
		result = prime * result + ((vakId == null) ? 0 : vakId.hashCode());
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
		Rooster other = (Rooster) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (lokaalId == null) {
			if (other.lokaalId != null)
				return false;
		} else if (!lokaalId.equals(other.lokaalId))
			return false;
		if (time != other.time)
			return false;
		if (vakId == null) {
			if (other.vakId != null)
				return false;
		} else if (!vakId.equals(other.vakId))
			return false;
		return true;
	}
	
}
