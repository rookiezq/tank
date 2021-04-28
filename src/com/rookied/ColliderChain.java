package com.rookied;

import java.util.LinkedList;
import java.util.List;

/**
 * @author zhangqiang
 * @date 2021/4/28
 */
public class ColliderChain implements Collider {
    List<Collider> list = new LinkedList<>();

    public ColliderChain() {
        /*String colliderChain = PropertyMgr.get("colliderChain");
        Arrays.stream(colliderChain.split(",")).forEach(s -> {
            try {
                add((Collider) Class.forName(s).newInstance());
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });*/
        add(new BulletTankCollider());
        add(new TankTankCollider());

    }

    public Collider add(Collider collider) {
        list.add(collider);
        return this;
    }

    @Override
    public boolean collide(GameObject o1, GameObject o2) {
        for(int i=0; i<list.size(); i++) {
            if(!list.get(i).collide(o1, o2)) {
                return false;
            }
        }
        return true;
    }
}
