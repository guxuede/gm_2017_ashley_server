package entityEdit;

import com.badlogic.ashley.core.Component;
import com.badlogic.ashley.core.Entity;
import com.guxuede.gm.net.system.component.ChannelComponent;
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
    public final <V  extends Component> T with(Class<V> component) {
        V c = edit.create(component);
        entity.add(c);
        return (T) this;
    }

    /**
     * Add artemis managed components to entity.
     */
    public final <V  extends Component> T with(Class<V> component, Consumer<V> consumer) {
        V c = edit.create(component);
        consumer.accept(c);
        entity.add(c);
        return (T) this;
    }


    public final T connectedToServer(Channel channel) {
        ChannelComponent channelComponent = edit.create(ChannelComponent.class);
        channelComponent.channel = channel;
        entity.add(channelComponent);
        return (T) this;
    }
}
