package eu.ownyourdata.pia.service;

import eu.ownyourdata.pia.domain.Item;
import eu.ownyourdata.pia.domain.Repo;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing Item.
 */
public interface ItemService {

    /**
     * Save a item.
     * @return the persisted entity
     */
    public JSONObject save(JSONObject json);


    public boolean belongs(Long id, String repoIdentifier);

    /**
     * Save a item.
     * @return the persisted entity
     */
    public Item save(Item item);


    /**
     *  get all the items.
     *  @return the list of entities
     */
    public Page<Item> findAll(Pageable pageable);

    public Page<Item> findAllByBelongs(Repo repo, Pageable pageable);

    /**
     *  get the "id" item.
     *  @return the entity
     */
    public JSONObject findOne(Long id);

    /**
     *  delete the "id" item.
     */
    public void delete(Long id);
}
