package net.rrm.ehour.ui.admin.project

import net.rrm.ehour.ui.common.model.AdminBackingBeanImpl
import net.rrm.ehour.domain.Project
import java.{util => ju}

class ProjectAdminBackingBean(private val project: Project) extends AdminBackingBeanImpl[Project] {
  val getProject: Project = project
  override def getDomainObject: Project = getProject

  def isNew = project.getPK == null

  private var assignExistingUsersToDefaultProjects: Boolean = false

  def isAssignExistingUsersToDefaultProjects: Boolean = assignExistingUsersToDefaultProjects

  def setAssignExistingUsersToDefaultProjects(assignExistingUsersToDefaultProjects: Boolean) {
    this.assignExistingUsersToDefaultProjects = assignExistingUsersToDefaultProjects
  }

}

