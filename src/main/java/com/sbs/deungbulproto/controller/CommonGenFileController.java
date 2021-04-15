package com.sbs.deungbulproto.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartRequest;

import com.sbs.deungbulproto.dto.GenFile;
import com.sbs.deungbulproto.dto.ResultData;
import com.sbs.deungbulproto.exceptions.GenFileNotFoundException;
import com.sbs.deungbulproto.service.GenFileService;

@Controller
public class CommonGenFileController extends BaseController {
	@Value("${custom.genFileDirPath}")
	private String genFileDirPath;

	@Autowired
	private GenFileService genFileService;

	@RequestMapping("/common/genFile/doUpload")
	@ResponseBody
	public ResultData doUpload(@RequestParam Map<String, Object> param, MultipartRequest multipartRequest) {
		return genFileService.saveFiles(param, multipartRequest);
	}

	@PostMapping("/common/genFile/doDelete")
	@ResponseBody
	public ResultData doDelete(@RequestParam Map<String, Object> param) {
		return genFileService.deleteFile(param);
	}

	@GetMapping("/common/genFile/file/{relTypeCode}/{relId}/{typeCode}/{type2Code}/{fileNo}")
	public ResponseEntity<Resource> showFile(HttpServletRequest request, @PathVariable String relTypeCode,
			@PathVariable int relId, @PathVariable String typeCode, @PathVariable String type2Code,
			@PathVariable int fileNo) throws FileNotFoundException {
		GenFile genFile = genFileService.getGenFile(relTypeCode, relId, typeCode, type2Code, fileNo);

		if (genFile == null) {
			throw new GenFileNotFoundException();
		}

		String filePath = genFile.getFilePath(genFileDirPath);
		Resource resource = new InputStreamResource(new FileInputStream(filePath));

		// Try to determine file's content type
		String contentType = request.getServletContext().getMimeType(new File(filePath).getAbsolutePath());

		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
	}
}
