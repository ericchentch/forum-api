package edunhnil.project.forum.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import edunhnil.project.forum.api.dto.commonDTO.CommonResponse;
import edunhnil.project.forum.api.dto.commonDTO.ListWrapperResponse;
import edunhnil.project.forum.api.dto.fileDTO.FileRequest;
import edunhnil.project.forum.api.dto.fileDTO.FileResponse;
import edunhnil.project.forum.api.service.fileService.FileService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping(value = "files")
public class FileController extends AbstractController<FileService> {
        @PostMapping(value = "user/create-file")
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<CommonResponse<String>> createFile(@RequestBody FileRequest fileRequest,
                        HttpServletRequest request) {
                validateToken(request, false);
                service.createFile(fileRequest);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Create file successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @GetMapping(value = "user/get-files")
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<CommonResponse<ListWrapperResponse<FileResponse>>> getFilesByUserId(
                        @RequestParam(required = false, defaultValue = "1") int page,
                        @RequestParam(required = false, defaultValue = "10") int pageSize,
                        @RequestParam Map<String, String> allParams,
                        @RequestParam(defaultValue = "asc") String keySort,
                        @RequestParam(defaultValue = "modified") String sortField, HttpServletRequest request) {
                String userId = validateToken(request, false);
                return response(service.getFilesByUserId(
                                userId,
                                page,
                                pageSize,
                                allParams,
                                keySort,
                                sortField), "Success");
        }

        @GetMapping(value = "user/get-file")
        @SecurityRequirement(name = "Bearer Authentication")
        public ResponseEntity<CommonResponse<FileResponse>> getFilesByFileId(
                        @RequestParam(required = true) String fileId,
                        HttpServletRequest request) {
                validateToken(request, false);
                return response(service.getFileById(fileId), "Success");
        }

        @DeleteMapping(value = "user/delete-file")
        @SecurityRequirement(name = "Bear Authentication")
        public ResponseEntity<CommonResponse<String>> deleteFileUser(@RequestParam(required = true) String _id,
                        HttpServletRequest request) {
                String loginId = validateToken(request, false);
                service.deleteFileUsers(_id, loginId);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete file successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }

        @DeleteMapping(value = "admin/delete-file")
        @SecurityRequirement(name = "Bear Authentication")
        public ResponseEntity<CommonResponse<String>> deleteFileAdmin(@RequestParam(required = true) String _id,
                        HttpServletRequest request) {
                validateToken(request, false);
                service.deleteFileAdmins(_id);
                return new ResponseEntity<CommonResponse<String>>(
                                new CommonResponse<String>(true, null, "Delete file successfully!",
                                                HttpStatus.OK.value()),
                                null,
                                HttpStatus.OK.value());
        }
}
