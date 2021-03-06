package net.rrm.ehour.ui.admin.assignment.form;

import net.rrm.ehour.domain.Project;
import net.rrm.ehour.ui.admin.assignment.AssignmentAdminBackingBean;
import org.apache.wicket.markup.html.form.AbstractChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.Collection;
import java.util.List;

public class CreateAssignmentProjectSelectionPanel extends AbstractAssignmentProjectSelectionPanel {
    public CreateAssignmentProjectSelectionPanel(String id, IModel<AssignmentAdminBackingBean> model) {
        super(id, model);
    }

    @Override
    protected AbstractChoice<?, Project> createProjectChoiceDropDown(IModel<List<Project>> projectChoices, String id) {
        return new ListMultipleChoice<Project>(id, new PropertyModel<Collection<Project>>(getDefaultModel(), "selectedProjects"), projectChoices, new ChoiceRenderer<Project>("fullName"));
    }

}
