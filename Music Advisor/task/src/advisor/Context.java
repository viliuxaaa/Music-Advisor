package advisor;

class ListUpdater {

    private UpdatingList method;

    // it may contain additional fields as well

    public void setMethod(UpdatingList method) {
        this.method = method;
    }

    public void update() {
        this.method.update();
    }
}
