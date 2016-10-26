/*
 * LICENCE : CloudUnit is available under the GNU Affero General Public License : https://gnu.org/licenses/agpl.html
 * but CloudUnit is licensed too under a standard commercial license.
 * Please contact our sales team if you would like to discuss the specifics of our Enterprise license.
 * If you are not sure whether the AGPL is right for you,
 * you can always test our software under the AGPL and inspect the source code before you contact us
 * about purchasing a commercial license.
 *
 * LEGAL TERMS : "CloudUnit" is a registered trademark of Treeptik and can't be used to endorse
 * or promote products derived from this project without prior written permission from Treeptik.
 * Products or services derived from this software may not be called "CloudUnit"
 * nor may "Treeptik" or similar confusing terms appear in their names without prior written permission.
 * For any questions, contact us : contact@treeptik.fr
 */


package fr.treeptik.cloudunit.service.impl;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.treeptik.cloudunit.dao.DeploymentDAO;
import fr.treeptik.cloudunit.exception.CheckException;
import fr.treeptik.cloudunit.exception.ServiceException;
import fr.treeptik.cloudunit.model.Application;
import fr.treeptik.cloudunit.model.Deployment;
import fr.treeptik.cloudunit.model.DeploymentType;
import fr.treeptik.cloudunit.service.ApplicationService;
import fr.treeptik.cloudunit.service.DeploymentService;

@Service
public class DeploymentServiceImpl implements DeploymentService {

    @Inject
    private DeploymentDAO deploymentDAO;

    @Inject
    private ApplicationService applicationService;

    @Override
    @Transactional
    public Deployment create(Application application, DeploymentType deploymentType, String contextPath)
            throws ServiceException, CheckException {
        try {
            Deployment deployment = application.addDeployment(contextPath, deploymentType);
                        
            applicationService.saveInDB(application);
            return deploymentDAO.save(deployment);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public Deployment find(Deployment deployment)
            throws ServiceException {
        try {
            return deploymentDAO.findOne(deployment.getId());
        } catch (PersistenceException e) {
            throw new ServiceException(e.getLocalizedMessage(), e);
        }
    }

    @Override
    public List<Deployment> findByApp(Application application)
            throws ServiceException {
        try {
            return deploymentDAO.findAllByApplication(application);
        } catch (PersistenceException e) {
            throw new ServiceException(e.getLocalizedMessage()
                    + application.getName(), e);
        }
    }
}
