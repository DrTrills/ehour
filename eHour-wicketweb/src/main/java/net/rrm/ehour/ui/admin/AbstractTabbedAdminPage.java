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

package net.rrm.ehour.ui.admin;

import net.rrm.ehour.domain.UserRole;
import net.rrm.ehour.ui.common.component.AddEditTabbedPanel;
import net.rrm.ehour.ui.common.model.AdminBackingBean;
import net.rrm.ehour.ui.common.panel.entryselector.EntrySelectorPanel;
import org.apache.wicket.Component;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeInstantiation;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.ResourceModel;

/**
 * Base admin page template with 2 tabs, add & edit
 */

@SuppressWarnings("serial")
@AuthorizeInstantiation(UserRole.ROLE_ADMIN)
public abstract class AbstractTabbedAdminPage<BB extends AdminBackingBean> extends AbstractAdminPage<BB> {
    private AddEditTabbedPanel<BB> tabbedPanel;

    public AbstractTabbedAdminPage(ResourceModel pageTitle,
                                   ResourceModel addTabTitle,
                                   ResourceModel editTabTitle,
                                   ResourceModel noEntrySelectedText) {
        super(pageTitle);

        tabbedPanel = new AddEditTabbedPanel<BB>("tabs", addTabTitle, editTabTitle, noEntrySelectedText) {
            @Override
            protected Panel getAddPanel(String panelId) {
                return getBaseAddPanel(panelId);
            }

            @Override
            protected Panel getEditPanel(String panelId) {
                return getBaseEditPanel(panelId);
            }

            @Override
            protected BB createAddBackingBean() {
                return getNewAddBaseBackingBean();
            }

            @Override
            protected BB createEditBackingBean() {
                return getNewEditBaseBackingBean();
            }

            @Override
            protected void onTabSwitch(int index) {
                AbstractTabbedAdminPage.this.onTabSwitch(index);
            }
        };

        add(tabbedPanel);
    }

    protected void onTabSwitch(int index) {

    }

    /**
     * Get the backing bean for the add panel
     *
     * @return
     */
    protected abstract BB getNewAddBaseBackingBean();

    /**
     * Get the backing bean for the edit panel
     *
     * @return
     */
    protected abstract BB getNewEditBaseBackingBean();

    /**
     * Get the panel for the add tab
     *
     * @param panelId
     * @return
     */
    protected abstract Panel getBaseAddPanel(String panelId);

    /**
     * Get the panel for the edit tab
     *
     * @param panelId
     * @return
     */
    protected abstract Panel getBaseEditPanel(String panelId);

    /**
     * @return the tabbedPanel
     */
    public AddEditTabbedPanel<BB> getTabbedPanel() {
        return tabbedPanel;
    }


    @Override
    public void onEvent(IEvent<?> event) {
        Object payload = event.getPayload();

        if (payload instanceof EntrySelectorPanel.FilterChangedEvent) {
            EntrySelectorPanel.FilterChangedEvent filterChangedEvent = (EntrySelectorPanel.FilterChangedEvent) payload;

            Component component = onFilterChanged(filterChangedEvent);

            if (component != null) {
                filterChangedEvent.refresh(component);
            }
        }
    }

    protected Component onFilterChanged(EntrySelectorPanel.FilterChangedEvent filterChangedEvent) {
        return null;
    }
}
