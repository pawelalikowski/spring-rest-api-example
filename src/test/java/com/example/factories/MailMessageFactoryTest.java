package com.example.factories;

import com.example.models.Mail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
public class MailMessageFactoryTest {

    private final MailMessageFactory factory = new MailMessageFactory();
    private Mail expected;

    @Before
    public void setUp() throws Exception {
        expected = new Mail();
        expected.setTo("test@example.com");
        expected.setTemplate("template.ftl");
        Map<String, Object> model = new HashMap<>();
        model.put("model","template.ftl");
        expected.setModel(model);
        expected.setSubject("Subject");
        expected.setFrom("from@example.com");
        expected.setBcc("bcc@example.com");
        expected.setCc("cc@example.com");
        expected.setReplyTo("noreply@example.com");
        expected.setSentDate(new Date(1));
    }

    @Test
    public void it_should_produce_mail_message_with_all_properties_set() {

        Mail mail = factory.create()
                .to(expected.getTo()[0])
                .from(expected.getFrom())
                .bcc(expected.getBcc())
                .cc(expected.getCc())
                .replyTo(expected.getReplyTo())
                .sentDate(expected.getSentDate())
                .template(expected.getTemplate())
                .addModel("model", "template.ftl")
                .subject(expected.getSubject())
                .build();

        assertEquals(expected, mail);
    }

    @Test
    public void it_should_complete_when_minimal_required_fields_are_set() {
        factory.create()
                .to("to")
                .subject("s")
                .template("s")
                .addModel("m", "m")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_when_to_is_not_set() {
        factory.create()
                .subject("s")
                .template("s")
                .addModel("m", "m")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_when_to_is_empty() {
        factory.create()
                .to("")
                .subject("s")
                .template("s")
                .addModel("m", "m")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_when_subject_is_not_set() {
        factory.create()
                .to("to")
                .template("s")
                .addModel("m", "m")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_when_subject_is_empty() {
        factory.create()
                .to("to")
                .subject("")
                .template("s")
                .addModel("m", "m")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_when_template_is_not_set() {
        factory.create()
                .to("to")
                .subject("s")
                .addModel("m", "m")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_when_template_is_empty() {
        factory.create()
                .to("to")
                .template("")
                .subject("s")
                .addModel("m", "m")
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void it_should_throw_when_model_is_empty() {
        factory.create()
                .to("to")
                .subject("s")
                .template("s")
                .build();
    }

    @Test
    public void it_should_build_message_with_model() {
        Mail mail = factory.create()
                .to("to")
                .addModel("model1", "model1")
                .addModel("model2", "model2")
                .subject("s")
                .template("s")
                .build();

        assertEquals("model1", mail.getModel().get("model1"));
        assertEquals("model2", mail.getModel().get("model2"));
    }
}
