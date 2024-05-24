package shop.catchmind.gpt.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import shop.catchmind.gpt.constant.GptConstant;
import shop.catchmind.gpt.dto.GptMessage;
import shop.catchmind.gpt.dto.GptResponse;
import shop.catchmind.gpt.dto.InterpretDto;
import shop.catchmind.gpt.dto.NaturalLanguageDto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class GptService {

    private final RestTemplate restTemplate;

    @Value(value = "${GPT_API_KEY}")
    private String apiKey;

    public InterpretDto interpretPicture(final NaturalLanguageDto dto) {
        List<GptMessage> messages = createGptMessages(dto);
        log.info("Request Messages: {}", messages);

        HashMap<String, Object> requestBody = createRequestBody(messages);

        GptResponse chatGptRes = getResponse(createHttpEntity(requestBody));

        String response = chatGptRes.getChoices().get(0).getMessage().content();
        log.info("Response: {}", response);

        return InterpretDto.builder().data(response).build();
    }

    // GPT 에 요청할 메시지를 만드는 메서드
    private static List<GptMessage> createGptMessages(final NaturalLanguageDto dto) {
        List<GptMessage> messages = new ArrayList<>();

        // gpt 역할(프롬프트) 설정
        messages.add(GptMessage.builder()
                .role(GptConstant.SYSTEM)
                .content(GptConstant.PROMPT)
                .build());

        // 실제 요청
        messages.add(GptMessage.builder()
                .role(GptConstant.USER)
                .content(dto.value())
                .build());
        return messages;
    }

    // GPT 에 요청할 파라미터를 만드는 메서드
    private static HashMap<String, Object> createRequestBody(final List<GptMessage> messages) {
        HashMap<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", GptConstant.CHAT_MODEL);
        requestBody.put("messages", messages);
        requestBody.put("max_tokens", GptConstant.MAX_TOKEN);
        requestBody.put("temperature", GptConstant.TEMPERATURE);
        return requestBody;
    }

    // api 호출에 필요한 Http Header를 만들고 HTTP 객체를 만드는 메서드
    public HttpEntity<HashMap<String, Object>> createHttpEntity(final HashMap<String, Object> chatGptRequest){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.parseMediaType(GptConstant.MEDIA_TYPE));
        httpHeaders.add(GptConstant.AUTHORIZATION, GptConstant.BEARER + apiKey);
        return new HttpEntity<>(chatGptRequest, httpHeaders);
    }

    // GPT API 요청후 response body를 받아오는 메서드
    public GptResponse getResponse(final HttpEntity<HashMap<String, Object>> httpEntity){

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        // 답변이 길어질 경우 TimeOut Error 발생하므로 time 설정
        requestFactory.setConnectTimeout(60000);
        requestFactory.setReadTimeout(60000);   //  1min

        restTemplate.setRequestFactory(requestFactory);
        ResponseEntity<GptResponse> responseEntity = restTemplate.exchange(
                GptConstant.CHAT_URL,
                HttpMethod.POST,
                httpEntity,
                GptResponse.class);

        return responseEntity.getBody();
    }
}
