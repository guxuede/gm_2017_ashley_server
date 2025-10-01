package entityEdit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.system.component.ChannelComponent;
import com.guxuede.gm.net.system.component.NetClientComponent;
import io.netty.channel.Channel;

import java.util.function.Consumer;

/**
 * Created by guxuede on 2017/6/10 .
 */
public abstract class EntityEditor<T extends EntityEditor>{

    private EntityEdit edit = new EntityEdit();
    private Entity entity;

    public T editEntity(Entity entity) {
        this.entity = entity;
        return (T) this;
    }

    public final T createEntity() {
        editEntity(edit.createEntity());
        return (T) this;
    }

    public final T removeEntity(Entity entity) {
        edit.engine.removeEntity(entity);
        return (T) this;
    }

    public Entity build() {
        final Entity tmp = this.entity;
        entity = null;
        return tmp;
    }

    public Entity buildToWorld() {
        edit.addToEngine(this.entity);
        final Entity tmp = this.entity;
        entity = null;
        return tmp;
    }

    /**
     * Add artemis managed components to entity.
     */
    public final T with(Class<? extends Component> component) {
        edit.create(component);
        return (T) this;
    }

    /**
     * Add artemis managed components to entity.
     */
    public final T withConfig(Class<? extends Component> component, Consumer<T> consumer) {
        Component component1 = edit.create(component);
        consumer.accept((T) component1);
        return (T) this;
    }


    public final T connectedToServer(Channel channel) {
        ChannelComponent channelComponent = edit.create(ChannelComponent.class);
        channelComponent.channel = channel;
        entity.add(channelComponent);
        NetClientComponent netClientComponent = edit.create(NetClientComponent.class);
        entity.add(netClientComponent);
        return (T) this;
    }
}
