package edunhnil.project.forum.api.dao.fileRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface FileRepository {
    void saveFile(File file);
    Optional<List<File>> getFiles(Map<String, String> allParams, int page, int pageSize);
    Optional<File> getFileById(String _id);
    // void deleteFile(File file);
}