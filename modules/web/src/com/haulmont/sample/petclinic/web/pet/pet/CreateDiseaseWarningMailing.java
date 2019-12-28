package com.haulmont.sample.petclinic.web.pet.pet;

import com.haulmont.sample.petclinic.entity.pet.PetType;
import com.haulmont.sample.petclinic.service.DiseaseWarningMailingService;
import com.haulmont.cuba.gui.Notifications;
import com.haulmont.cuba.gui.components.Action;
import com.haulmont.cuba.gui.components.LookupField;
import com.haulmont.cuba.gui.components.TextField;
import com.haulmont.cuba.gui.screen.EditorScreen;
import com.haulmont.cuba.gui.screen.Screen;
import com.haulmont.cuba.gui.screen.StandardCloseAction;
import com.haulmont.cuba.gui.screen.Subscribe;
import com.haulmont.cuba.gui.screen.UiController;
import com.haulmont.cuba.gui.screen.UiDescriptor;
import javax.inject.Inject;

@UiController("petclinic_CreateDiseaseWarningMailing")
@UiDescriptor("create-disease-warning-mailing.xml")
public class CreateDiseaseWarningMailing extends Screen {


  @Subscribe
  protected void onBeforeShow(BeforeShowEvent beforeShowEvent) {
    getScreenData().loadAll();
  }


  @Inject
  protected DiseaseWarningMailingService diseaseWarningMailingService;

  @Inject
  protected TextField<String> city;

  @Inject
  protected TextField<String> disease;

  @Inject
  protected LookupField<PetType> petType;

  @Inject
  protected Notifications notifications;

  @Subscribe("createDiseaseWarningMailing")
  protected void createDiseaseWarningMailing(Action.ActionPerformedEvent event) {

    int endangeredPets = diseaseWarningMailingService.warnAboutDisease(
        petType.getValue(),
        disease.getValue(),
        city.getValue()
    );

    close(new StandardCloseAction(EditorScreen.WINDOW_COMMIT_AND_CLOSE))
        .then(() ->
            notifications.create()
                .withCaption(endangeredPets + " Owner(s) of endangered Pets have been notified")
                .withType(Notifications.NotificationType.TRAY)
                .show()
        );
  }
}