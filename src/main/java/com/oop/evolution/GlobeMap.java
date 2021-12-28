package com.oop.evolution;

public class GlobeMap extends AbstractMap{

    protected GlobeMap(int width, int height, double jungleRatio, int plantEnergySource) {
        super(width, height, jungleRatio, plantEnergySource);
    }

    @Override
    public Vector2d moveTo(Vector2d position, Vector2d oldPosition) {
        if(!(position.hasGreaterParameter(getSize())
                || position.hasSmallerParameter(new Vector2d(0, 0)))){
            return position;
        }

        int xWrapped = position.x;
        int yWrapped = position.y;

        if(xWrapped>=this.getSize().x){
            xWrapped = 0;
        }
        else if(xWrapped < 0){
            xWrapped = this.getSize().x;
        }

        if(yWrapped>=this.getSize().y){
            yWrapped = 0;
        }
        else if(yWrapped < 0){
            yWrapped = this.getSize().y;
        }
        return new Vector2d(xWrapped,yWrapped);

    }

}
