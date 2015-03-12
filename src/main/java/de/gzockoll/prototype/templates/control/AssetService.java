package de.gzockoll.prototype.templates.control;

import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.Collection;

@Transactional
@Controller
@Slf4j
public class AssetService {
    @Autowired
    private AssetRepository repository;

    public Collection<? extends Asset> findByMimeType(String s) {
        return repository.findByMimeType(s);
    }
}
