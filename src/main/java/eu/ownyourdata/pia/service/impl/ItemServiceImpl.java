package eu.ownyourdata.pia.service.impl;

import eu.ownyourdata.pia.domain.Item;
import eu.ownyourdata.pia.domain.Repo;
import eu.ownyourdata.pia.repository.ItemRepository;
import eu.ownyourdata.pia.service.ItemService;
import eu.ownyourdata.pia.web.rest.mapper.ItemMapper;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;

/**
 * Service Implementation for managing Item.
 */
@Service
@Transactional
public class ItemServiceImpl implements ItemService{

    private final Logger log = LoggerFactory.getLogger(ItemServiceImpl.class);

    @Inject
    private ItemRepository itemRepository;

    @Inject
    private ItemMapper itemMapper;

    /**
     * Save a item.
     * @return the persisted entity
     */
    public JSONObject save(JSONObject json) {
        log.debug("Request to save Item : {}", json);
        Item item = itemMapper.jsonToItem(json);
        item = itemRepository.save(item);
        JSONObject result = itemMapper.itemToJson(item);
        return result;
    }

    /**
     * Save a item.
     * @return the persisted entity
     */
    public Item save(Item item) {
        log.debug("Request to save Item : {}", item);

        return itemRepository.save(item);
    }

    /**
     *  get all the items.
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<Item> findAll(Pageable pageable) {
        log.debug("Request to get all Items");
        Page<Item> result = itemRepository.findAll(pageable);
        return result;
    }

    @Override
    public Page<Item> findAllByBelongs(Repo repo, Pageable pageable) {
        log.debug("Request to get all Items");
        return itemRepository.findAllByBelongs(repo,pageable);
    }

    /**
     *  get one item by id.
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public JSONObject findOne(Long id) {
        log.debug("Request to get Item : {}", id);
        Item item = itemRepository.findOne(id);

        return item == null ? null : itemMapper.itemToJson(item);
    }

    @Override
    public boolean belongs(Long id, String repoIdentifier) {
        return itemRepository.findOne(id).getBelongs().getIdentifier().equals(repoIdentifier);
    }

    /**
     *  delete the  item by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Item : {}", id);
        itemRepository.delete(id);
    }
}
