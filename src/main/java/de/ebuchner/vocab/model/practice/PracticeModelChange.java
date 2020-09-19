package de.ebuchner.vocab.model.practice;

import de.ebuchner.vocab.model.commands.SimpleCommand;

public class PracticeModelChange implements SimpleCommand {
    private PracticeImage image, oldImage;
    private PracticeModel model;
    private PracticeReverse reverse, oldReverse;
    private SelectedStrategy selectedStrategy, oldSelectedStrategy;
    private Boolean showStatistics, oldShowStatistics;

    public PracticeModelChange(PracticeModel model, Boolean showStatistics) {
        this.model = model;
        this.showStatistics = showStatistics;
    }

    public PracticeModelChange(PracticeModel model, PracticeReverse reverse) {
        this.model = model;
        this.reverse = reverse;
    }

    public PracticeModelChange(PracticeModel model, SelectedStrategy selectedStrategy) {
        this.model = model;
        this.selectedStrategy = selectedStrategy;
    }

    public PracticeModelChange(PracticeModel model, PracticeImage image) {
        this.model = model;
        this.image = image;
    }


    public void execute() {
        if (reverse != null) {
            oldReverse = model.getReverse();
            model.setReverse(reverse);
        } else if (showStatistics != null) {
            oldShowStatistics = showStatistics;
            model.setShowStatistics(showStatistics);
        } else if (selectedStrategy != null) {
            oldSelectedStrategy = model.getSelectedStrategy();
            model.setSelectedStrategy(selectedStrategy);
        } else if (image != null) {
            oldImage = model.getImageOption();
            model.setImageOption(image);
        }
    }

    public void unExecute() {
        if (oldReverse != null) {
            model.setReverse(oldReverse);
        } else if (oldShowStatistics != null) {
            model.setShowStatistics(oldShowStatistics);
        } else if (oldSelectedStrategy != null) {
            model.setSelectedStrategy(oldSelectedStrategy);
        } else if (oldImage != null) {
            model.setImageOption(oldImage);
        }
    }
}
