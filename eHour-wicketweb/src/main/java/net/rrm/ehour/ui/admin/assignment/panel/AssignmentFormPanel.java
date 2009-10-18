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

package net.rrm.ehour.ui.admin.assignment.panel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.rrm.ehour.config.EhourConfig;
import net.rrm.ehour.customer.service.CustomerService;
import net.rrm.ehour.domain.Customer;
import net.rrm.ehour.domain.Project;
import net.rrm.ehour.exception.ObjectNotFoundException;
import net.rrm.ehour.exception.ObjectNotUniqueException;
import net.rrm.ehour.exception.ParentChildConstraintException;
import net.rrm.ehour.exception.PasswordEmptyException;
import net.rrm.ehour.project.service.ProjectAssignmentManagementService;
import net.rrm.ehour.ui.admin.assignment.dto.AssignmentAdminBackingBean;
import net.rrm.ehour.ui.common.ajax.AjaxEventType;
import net.rrm.ehour.ui.common.border.GreySquaredRoundedBorder;
import net.rrm.ehour.ui.common.component.AjaxFormComponentFeedbackIndicator;
import net.rrm.ehour.ui.common.component.ServerMessageLabel;
import net.rrm.ehour.ui.common.component.ValidatingFormComponentAjaxBehavior;
import net.rrm.ehour.ui.common.form.FormUtil;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.AbstractFormSubmittingPanel;
import net.rrm.ehour.ui.common.session.EhourWebSession;
import net.rrm.ehour.ui.common.sort.ProjectComparator;
import net.rrm.ehour.ui.common.util.WebGeo;

import org.apache.log4j.Logger;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

/**
 * Assignment form (and yes, it's a little (too) big & complex)
 **/

@SuppressWarnings("serial")
public class AssignmentFormPanel extends AbstractFormSubmittingPanel
{
	private static final long serialVersionUID = -85486044225123470L;
	private	final static Logger	logger = Logger.getLogger(AssignmentFormPanel.class);
	
	@SpringBean
	private CustomerService	customerService;
	@SpringBean
	private ProjectAssignmentManagementService projectAssignmentManagementService;
	protected EhourConfig		config;
	/**
	 * 
	 * @param id
	 * @param model
	 * @param customers
	 * @param assignmenTypes
	 */
	public AssignmentFormPanel(String id, final IModel model)
	{
		super(id, model);
		
		config = EhourWebSession.getSession().getEhourConfig();
		
		setOutputMarkupId(true);
		
		setUpPage(this, model);
	}

	/*
	 * (non-Javadoc)
	 * @see net.rrm.ehour.ui.common.panel.noentry.AbstractAjaxAwareAdminPanel#processFormSubmit(net.rrm.ehour.ui.common.model.AdminBackingBean, int)
	 */
	@Override
	protected void processFormSubmit(AjaxRequestTarget target, AdminBackingBean backingBean, AjaxEventType type) throws Exception
	{
		AssignmentAdminBackingBean assignmentBackingBean = (AssignmentAdminBackingBean) backingBean;
		
		if (type == AssignmentAjaxEventType.ASSIGNMENT_UPDATED)
		{
			persistAssignment(assignmentBackingBean);
		}
		else if (type == AssignmentAjaxEventType.ASSIGNMENT_DELETED)
		{
			deleteAssignment(assignmentBackingBean);
		}		
	}		
	
	/**
	 * Persist user
	 * @param userBackingBean
	 * @throws ObjectNotUniqueException 
	 * @throws PasswordEmptyException 
	 */
	private void persistAssignment(AssignmentAdminBackingBean backingBean)
	{
		projectAssignmentManagementService.assignUserToProject(backingBean.getProjectAssignmentForSave());
	}
	
	/**
	 * 
	 * @param backingBean
	 * @throws ParentChildConstraintException 
	 * @throws ObjectNotFoundException 
	 */
	private void deleteAssignment(AssignmentAdminBackingBean backingBean) throws ObjectNotFoundException, ParentChildConstraintException
	{
		projectAssignmentManagementService.deleteProjectAssignment(backingBean.getProjectAssignment().getAssignmentId());
	}
	
	
	/**
	 * Setup form
	 */
	protected void setUpPage(WebMarkupContainer parent, final IModel model)
	{
		Border greyBorder = new GreySquaredRoundedBorder("border", WebGeo.W_CONTENT_SMALL);
		add(greyBorder);
		
		final Form form = new Form("assignmentForm");
		greyBorder.add(form);
		
		Component[] projectDependentComponents = addProjectDuration(form, model);
		
		// setup the customer & project dropdowns
		addCustomerAndProjectChoices(form, model, projectDependentComponents);
		
		// Add rate & role
		addRateRole(form, model);
		
		// active
		form.add(new CheckBox("projectAssignment.active"));
		
		// data save label
		form.add(new ServerMessageLabel("serverMessage", "formValidationError"));
		
		// add submit form
		FormUtil.setSubmitActions(form 
									,((AssignmentAdminBackingBean)model.getObject()).getProjectAssignment().isDeletable() 
									,this
									,AssignmentAjaxEventType.ASSIGNMENT_UPDATED
									,AssignmentAjaxEventType.ASSIGNMENT_DELETED
									,config);
		
		parent.add(greyBorder);
	}

	/**
	 * Add rate, role & active
	 * @param form
	 * @param model
	 */
	private void addRateRole(Form form, final IModel model)
	{
		form.add(new AssignmentRateRoleFormPartPanel("rateRole", model));
	}
	
	/**
	 * Add project duration
	 * @param form
	 * @param model
	 * @return
	 */
	protected Component[] addProjectDuration(Form form, final IModel model)
	{
		AssignmentTypeFormPartPanel typePanel = new AssignmentTypeFormPartPanel("assignmentType", model, form);
		form.add(typePanel);
		
		// assignment type
		Component[] projectDependentComponents = typePanel.getNotifiableComponents();
		
		return projectDependentComponents;
	}
	
	/**
	 * Add customer & project dd's
	 * @param form
	 * @param model
	 * @param customers
	 */
	protected void addCustomerAndProjectChoices(Form form, 
											final IModel model,
											final Component[] projectDependentComponents)
	{
		List<Customer> customers = customerService.getCustomers(true);
		
		// customer
		DropDownChoice customerChoice = new DropDownChoice("customer", customers, new ChoiceRenderer("fullName"));
		customerChoice.setRequired(true);
		customerChoice.setNullValid(false);
		customerChoice.setLabel(new ResourceModel("admin.assignment.customer"));
		form.add(customerChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("customerValidationError", customerChoice));

		// project model
		IModel	projectChoices = new AbstractReadOnlyModel()
		{
			@Override
			public Object getObject()
			{
				// need to re-get it, project set is lazy
				Customer selectedCustomer = ((AssignmentAdminBackingBean)model.getObject()).getCustomer();
				Customer customer = null;
				
				if (selectedCustomer != null)
				{
					try
					{
						customer = customerService.getCustomer(selectedCustomer.getCustomerId());
					} catch (ObjectNotFoundException e)
					{
						// TODO
					}
				}
				
				if (customer == null || customer.getProjects() == null || customer.getProjects().size() == 0)
				{
					logger.debug("Empty project set for customer: " + customer);
					return Collections.EMPTY_LIST;
				}
				else
				{
					List<Project> projects = new ArrayList<Project>(customer.getActiveProjects());
					
					Collections.sort(projects, new ProjectComparator());
					
					return projects;
				}
			}
		};
		
		// project
		final DropDownChoice projectChoice = new DropDownChoice("projectAssignment.project", projectChoices, new ChoiceRenderer("fullName"));
		projectChoice.setRequired(true);
		projectChoice.setOutputMarkupId(true);
		projectChoice.setNullValid(false);
		projectChoice.setLabel(new ResourceModel("admin.assignment.project"));
		projectChoice.add(new ValidatingFormComponentAjaxBehavior());
		form.add(projectChoice);
		form.add(new AjaxFormComponentFeedbackIndicator("projectValidationError", projectChoice));

		customerChoice.add(new ValidatingFormComponentAjaxBehavior());

		// make project update automatically when customers changed
		customerChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			protected void onUpdate(AjaxRequestTarget target)
            {
                target.addComponent(projectChoice);
            }
        });	


		projectChoice.add(new ValidatingFormComponentAjaxBehavior());

		// update any components that showed interest
		projectChoice.add(new AjaxFormComponentUpdatingBehavior("onchange")
        {
			protected void onUpdate(AjaxRequestTarget target)
            {
				for (Component component : projectDependentComponents)
				{
					target.addComponent(component);	
				}
            }
        });	
	}
}
