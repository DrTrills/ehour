package net.rrm.ehour.domain;

import java.util.Date;

/**
 * Created on Feb 7, 2010 2:47:20 PM
 *
 * @author thies (www.te-con.nl)
 */
public class TimesheetEntryObjectMother {
    public static TimesheetEntry createTimesheetEntry(int prjId, Date date, float hours) {
        TimesheetEntryId id = new TimesheetEntryId();
        id.setEntryDate(date);
        id.setProjectAssignment(ProjectAssignmentObjectMother.createProjectAssignment(prjId, prjId, 1));

        TimesheetEntry entry = new TimesheetEntry();
        entry.setEntryId(id);
        entry.setHours(hours);

        return entry;
    }
}
