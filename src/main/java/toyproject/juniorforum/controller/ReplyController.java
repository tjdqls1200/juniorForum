package toyproject.juniorforum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import toyproject.juniorforum.domain.DTO;
import toyproject.juniorforum.exception.ReplyNotFoundException;
import toyproject.juniorforum.service.ReplyService;

import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.Collections;
import java.util.List;

import static toyproject.juniorforum.domain.DTO.*;
import static toyproject.juniorforum.domain.Paging.*;
import static toyproject.juniorforum.domain.VO.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/replies")
public class ReplyController {
    private final ReplyService replyService;

    @GetMapping("{boardId}/{replyId}")
    public ReplyVO read(@PathVariable int boardId, @PathVariable int replyId) {
        ReplyVO read = replyService.read(replyId);
        log.info("read : {}", read);
        return read;
    }

    @GetMapping("/{boardId}/paging/{pageNum}")
    public ReplyPagingDTO list(Criteria criteria, @PathVariable int boardId, @PathVariable int pageNum) {
        log.info("--------- Reply List --------");
        return replyService.getList(criteria, boardId, pageNum);
    }

    @PostMapping
    public ResponseEntity<ReplyDTO> create(@Validated @RequestBody ReplyDTO replyDTO,
                                           BindingResult bindingResult) {
        log.info("------------ test --------------");
        final int replyId = replyService.create(replyDTO);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{replyId}")
                .buildAndExpand(replyId)
                .toUri();
        log.info("--------- reply create --------");
        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<String> update(@PathVariable int boardId, @Validated @RequestBody ReplyDTO replyDTO,
                                           BindingResult bindingResult) {
        replyService.update(replyDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> delete(@PathVariable int replyId) {
        log.info("------------ reply delete ----------");
        replyService.delete(replyId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
