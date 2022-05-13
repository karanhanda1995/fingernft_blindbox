package com.fingerchar.admin.service;

import com.fingerchar.core.util.CharUtil;
import com.fingerchar.core.storage.IpfsStorage;
import com.fingerchar.core.storage.Storage;
import com.fingerchar.db.domain.FcStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;



/**
 * 提供存储服务类，所有存储服务均由该类对外提供
 */
@Service
public class StorageService {
    
    private Storage storage;

    private Storage ipfsStorage;
    
    @Autowired
    private com.fingerchar.admin.service.FcStorageService fcStorageService;

	public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

	public Storage getIpfsStorage() {
		return ipfsStorage;
	}

	public void setIpfsStorage(Storage ipfsStorage) {
		this.ipfsStorage = ipfsStorage;
	}

	/**
     * 存储一个文件对象
     *
     * @param inputStream   文件输入流
     * @param contentLength 文件长度
     * @param contentType   文件类型
     * @param fileName      文件索引名
     */
    public FcStorage store(InputStream inputStream, long contentLength, String contentType, String fileName, String flag) {
    	if ("ipfs".equals(flag)){
				String key = generateKey(fileName);
				String fileHash = ipfsStorage.store(inputStream, key);
				if(null == fileHash) {
					return null;
				} else {
					FcStorage storageInfo = new FcStorage();
					storageInfo.setName(fileName);
					storageInfo.setSize((int) contentLength);
					storageInfo.setType(contentType);
					storageInfo.setKey(key);
					storageInfo.setUrl(this.ipfsStorage.generateUrl(fileHash));
					storageInfo.setCreateTime(System.currentTimeMillis()/1000);
					storageInfo.setUpdateTime(System.currentTimeMillis()/1000);
					storageInfo.setIpfshash("ipfs://ipfs/" + fileHash.substring(0, fileHash.indexOf(".")));
					fcStorageService.add(storageInfo);
					return storageInfo;
				}
		}
    	if(storage instanceof IpfsStorage) {
    		String key = generateKey(fileName);
    		String fileHash = storage.store(inputStream, key);
    		if(null == fileHash) {
    			return null;
    		} else {
    			FcStorage storageInfo = new FcStorage();
    			storageInfo.setName(fileName);
    			storageInfo.setSize((int) contentLength);
    			storageInfo.setType(contentType);
    			storageInfo.setKey(key);
    			storageInfo.setUrl(this.storage.generateUrl(fileHash));
    			storageInfo.setCreateTime(System.currentTimeMillis()/1000);
    	        storageInfo.setUpdateTime(System.currentTimeMillis()/1000);
    	        storageInfo.setIpfshash("ipfs://ipfs/" + fileHash.substring(0, fileHash.indexOf(".")));
    			fcStorageService.add(storageInfo);
    			return storageInfo;
    		}
    	} else {
	        String key = generateKey(fileName);
	        storage.store(inputStream, contentLength, contentType, key);
	
	        String url = generateUrl(key);
	        FcStorage storageInfo = new FcStorage();
	        storageInfo.setName(fileName);
	        storageInfo.setSize((int) contentLength);
	        storageInfo.setType(contentType);
	        storageInfo.setKey(key);
	        storageInfo.setUrl(url);
	        storageInfo.setCreateTime(System.currentTimeMillis()/1000);
	        storageInfo.setUpdateTime(System.currentTimeMillis()/1000);
	        fcStorageService.add(storageInfo);
	
	        return storageInfo;
    	}
    }
    
    public List<FcStorage> store(InputStream[] inputStreams,Long[] contentLengths, String[] contentTypes, String[] fileNames) {
    	if(ipfsStorage instanceof IpfsStorage) {
    		String basePath = generateUUID();
    		int len = fileNames.length;
			String[] keys = new String[len];
			keys[0] = "animation" + fileNames[0].substring(fileNames[0].lastIndexOf("."));
			keys[1] = "image" + fileNames[1].substring(fileNames[1].lastIndexOf("."));
    		String[] hashFiles = ipfsStorage.store(inputStreams, keys, basePath);
    		if(null == hashFiles) {
    			return null;
    		} else {
    			
    			FcStorage storageInfo = null;
    			List<FcStorage> list = new ArrayList<>();
    			for(int i=0; i<len; i++) {
    				storageInfo = new FcStorage();
    				storageInfo.setName(fileNames[i]);
        			storageInfo.setSize(contentLengths[i].intValue());
        			storageInfo.setType(contentTypes[i]);
        			storageInfo.setKey(keys[i]);
        			storageInfo.setUrl(this.ipfsStorage.generateUrl(hashFiles[i]));
        			storageInfo.setCreateTime(System.currentTimeMillis()/1000);
        	        storageInfo.setUpdateTime(System.currentTimeMillis()/1000);
        	        storageInfo.setIpfshash("ipfs://ipfs/" + hashFiles[i].substring(0, hashFiles[i].indexOf("/")));
        			fcStorageService.add(storageInfo);
        			list.add(storageInfo);
    			}
    			return list;
    		}
    	} else {    		
    		return null;
    	}
    }

    private String generateKey(String originalFilename) {
        int index = originalFilename.lastIndexOf('.');
        String suffix = originalFilename.substring(index);

        String key = null;
        FcStorage storageInfo = null;

        do {
            key = CharUtil.getRandomString(20) + suffix;
            storageInfo = fcStorageService.findByKey(key);
        }
        while (storageInfo != null);

        return key;
    }
    
    private String generateUUID() {
    	return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public Stream<Path> loadAll() {
        return storage.loadAll();
    }

    public Path load(String keyName) {
        return storage.load(keyName);
    }

    public Resource loadAsResource(String keyName) {
        return storage.loadAsResource(keyName);
    }

    public void delete(String keyName) {
        storage.delete(keyName);
    }

    private String generateUrl(String keyName) {
        return storage.generateUrl(keyName);
    }
}
