package uet.oop.bomberman.entities.enemies;

import javafx.scene.image.Image;
import uet.oop.bomberman.controller.collision.CollisionManager;
import uet.oop.bomberman.controller.collision.Graph;
import uet.oop.bomberman.controller.collision.Vertices;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.graphics.sprite.Sprite;

import java.util.ArrayList;
import java.util.List;

public class Oneal extends Enemy {
    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    public enum OnealStatus {
        CHASING,
        WALKING,
        INVALID
    }
    OnealStatus onealStatus;

    private Entity bomber;
    private List<List<Entity>> map;
    private List<Vertices> path;

    public Oneal(int x, int y, Image img, CollisionManager collisionManager, Entity bomber) {
        super(x, y, img, collisionManager);
        onealStatus = OnealStatus.WALKING;
        this.speed = 1;
        this.map = collisionManager.getMap().getMap();
        this.bomber = bomber;
    }

    public int getDistanceFromBomber() {
        if (path == null) return -1;
        return path.size();
    }

    public void chasing() {
        Vertices src = path.get(0);
        Vertices dst = path.get(1);
        spriteIndex++;

        if (src.getXTilePos() >= dst.getXTilePos()) {
            if (x > dst.getXTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.touchObstacle(x, y, "LEFT", speed)) {
                    pickSprite(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], spriteIndex, 20).getFxImage());
                    x -= speed;
                }
            }
        }

        if (src.getXTilePos() <= dst.getXTilePos()) {
            if (x < dst.getXTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.touchObstacle(x, y, "RIGHT", speed)) {
                    pickSprite(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], spriteIndex, 20).getFxImage());
                    x += speed;
                }
            }
        }

        if (src.getYTilePos() >= dst.getYTilePos()) {
            if (y > dst.getYTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.touchObstacle(x, y, "UP", speed)) {
                    pickSprite(Sprite.movingSprite(
                            rightSprites[0],
                            rightSprites[1],
                            rightSprites[2], spriteIndex, 20).getFxImage());
                    y -= speed;
                }
            }
        }

        if (src.getYTilePos() <= dst.getYTilePos()) {
            if (y < dst.getYTilePos() * Sprite.SCALED_SIZE) {
                if (!collisionManager.touchObstacle(x, y, "DOWN", speed)) {
                    pickSprite(Sprite.movingSprite(
                            leftSprites[0],
                            leftSprites[1],
                            leftSprites[2], spriteIndex, 20).getFxImage());
                    y += speed;
                }
            }
        }
    }

    public void move() {
        int onealIndex = Graph.getVerticesIndex(x + Oneal.WIDTH / 2, y + Oneal.HEIGHT / 2);
        int bomberIndex = Graph.getVerticesIndex(bomber.getX(), bomber.getY());

        if (onealStatus == OnealStatus.WALKING) {
            path = collisionManager.getMap().getGraph().BFS(onealIndex, bomberIndex);
            if (path != null) onealStatus = OnealStatus.CHASING;
        }

        if (onealStatus != OnealStatus.CHASING) {
            goRandom();
        } else {
            path = collisionManager.getMap().getGraph().BFS(onealIndex, bomberIndex);
            if (path != null) {
                chasing();
            }
        }
    }

    public void setOnealStatus(OnealStatus onealStatus) {
        this.onealStatus = onealStatus;
    }

    public OnealStatus getOnealStatus() {
        return onealStatus;
    }
}
