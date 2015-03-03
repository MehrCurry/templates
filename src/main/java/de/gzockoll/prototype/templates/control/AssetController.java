package de.gzockoll.prototype.templates.control;

import com.google.common.base.Preconditions;
import de.gzockoll.prototype.templates.entity.Asset;
import de.gzockoll.prototype.templates.entity.AssetRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@RestController
@Slf4j
@Transactional
public class AssetController {

    @Autowired
    private AssetRepository repository;

    /**
     * Adds a document to the archive.
     *
     * Url: /archive/upload?file={file}&person={person}&date={date} [POST]
     *
     * @param file A file posted in a multipart request
     * @param person The name of the uploading person
     * @param date The date of the document
     * @return The meta data of the added document
     */
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody
    Asset handleFileUpload(
            @RequestParam(value="file", required=true) MultipartFile file) {

        Asset asset = null;
        try {
            int sizeUpload = file.getBytes().length;
            asset = new Asset(file.getInputStream(),file.getOriginalFilename());
            int sizeAsset=asset.getData().length;
            Preconditions.checkState(sizeUpload==sizeAsset);
            repository.save(asset);
        } catch (IOException e) {
            log.error("Error: " + e);
        }
        return asset;
    }

    @RequestMapping(value = "/assets/{id}", method = RequestMethod.GET)
    public HttpEntity<Asset> findDocument(@PathVariable(value = "id") Long id) {
        HttpHeaders httpHeaders = new HttpHeaders();
        final Asset asset = repository.findOne(id);
        return new ResponseEntity<>(asset, httpHeaders, HttpStatus.OK);
    }

    @RequestMapping(value = "/assets/raw/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> sendDocument(@PathVariable(value = "id") Long id) {
        final Asset asset = repository.findOne(id);
        long size=asset.getData().length;
        log.debug("Size " + size);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(asset.getMimeType()));
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<byte[]>(asset.getData(), headers, HttpStatus.OK);
        return response;
    }

    public void save(Asset a) {
        Preconditions.checkNotNull(a);
        repository.save(a);
    }
}
