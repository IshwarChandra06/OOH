//package com.eikona.tech.api.controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import com.eikona.tech.service.impl.VideoStreamService;
//
//import reactor.core.publisher.Mono;
//
//@Controller
//@RequestMapping("/video")
//public class VideoStreamController {
//
//    private final VideoStreamService videoStreamService;
//
//    public VideoStreamController(VideoStreamService videoStreamService) {
//        this.videoStreamService = videoStreamService;
//    }
//    
//
//    @GetMapping("/stream/{fileType}/{fileName}")
//    public Mono<ResponseEntity<byte[]>> streamVideo(ServerHttpResponse serverHttpResponse, @RequestHeader(value = "Range", required = false) String httpRangeList,
//                                                    @PathVariable("fileType") String fileType,
//                                                    @PathVariable("fileName") String fileName) {
//        return Mono.just(videoStreamService.prepareContent(fileName, fileType, httpRangeList));
//    }
//    
//    @GetMapping("/play")
//	public String video() {
//		return "play_video";
//	}
//}
