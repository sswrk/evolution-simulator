package main;

public interface IPositionChangeObserver {

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, Animal a);

}