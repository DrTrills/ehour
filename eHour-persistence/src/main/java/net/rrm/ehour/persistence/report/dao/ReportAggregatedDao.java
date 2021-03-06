/*
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */

package net.rrm.ehour.persistence.report.dao;

import net.rrm.ehour.data.DateRange;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.domain.ProjectAssignment;
import net.rrm.ehour.domain.User;
import net.rrm.ehour.report.reports.element.AssignmentAggregateReportElement;

import java.io.Serializable;
import java.util.List;

/**
 * Reporting data operations
 *
 * @author Thies
 */

public interface ReportAggregatedDao {
    /**
     * Get cumulated hours per project assignment for user in a date range
     *
     * @param userId
     * @param dateRange
     * @return
     */
    List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users, DateRange dateRange);

    /**
     * Get cumulated hours per project assignment for users
     *
     * @param userId
     * @param dateRange
     * @return
     */
    List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users);

    /**
     * Get cumulated hours per project assignment for assignments
     *
     * @param projectAssignmentIds
     * @return
     */
    List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForAssignments(List<? extends Serializable> projectAssignmentIds);

    /**
     * Get cumulated hours per project assignment for users, projects
     *
     * @param userId
     * @param projectId
     * @return
     */
    List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users, List<Project> projects);

    /**
     * Get cumulated hours per project assignment for users, projects in a date range
     *
     * @param userId
     * @param projectId
     * @param dateRange
     * @return
     */
    List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForUsers(List<User> users, List<Project> projects, DateRange dateRange);

    /**
     * Get cumulated hours per project assignment for all users, projects in a date range
     *
     * @param projectId
     * @param dateRange
     * @return
     */
    List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignmentForProjects(List<Project> projects, DateRange dateRange);

    /**
     * Get cumulated hours for a project assignment
     *
     * @param userId
     * @param projectAssignmentId
     * @return
     */
    AssignmentAggregateReportElement getCumulatedHoursForAssignment(ProjectAssignment assignment);

    /**
     * Get cumulated hours
     *
     * @param dateRange
     * @return
     */
    List<AssignmentAggregateReportElement> getCumulatedHoursPerAssignment(DateRange dateRange);

    /**
     * Get the min/max timesheet date
     *
     * @return
     */
    DateRange getMinMaxDateTimesheetEntry();

    /**
     * Get the min/max timesheet date for a user
     *
     * @return
     */
    DateRange getMinMaxDateTimesheetEntry(User user);

    /**
     * Get the min/max timesheet date for a project
     *
     * @param project
     * @return
     */
    DateRange getMinMaxDateTimesheetEntry(Project project);

    /**
     * Get assignments without bookings for a particular range
     *
     * @param dateRange
     * @return
     */
    List<ProjectAssignment> getAssignmentsWithoutBookings(DateRange dateRange);

}
