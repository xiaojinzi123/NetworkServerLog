package com.xiaojinzi.controller;

import com.google.gson.Gson;
import com.xiaojinzi.bean.DataWrap;
import com.xiaojinzi.util.RSAAuth;
import com.xiaojinzi.util.RSAUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

/**
 * Created by cxj on 16/01/2018.
 */
@RequestMapping("rsa")
@Controller
public class RSAAuthController {

    private PublicKey publicKey = RSAUtil.string2PublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxddg1MJrsjX/2gCk2PN9yfykOBrCB2ijMwmspzwFYhtP4azDFer7CZ03NoT6Wr7g+6xmx9PlFaXfuhSA0pH9sYIjpzAz5okvt8WZtDTjK7xBpLy/SHvhhN24NHQNJ9nF7t1X3vh+W4Xx5rMHzK9C4rQyPHVHrQZsWUvgnCMH+81QoqefzQDbQnFdHtWVCo+vN41gPL32WW27Kzi3y1HonRkwN4W878SEi1oY/GjZIEAN5hX3LdausiSzy8Shku6NyL5Tpp+CODMiJvTiNgp4DOTv/jzDj+xlBORMjIoqq82EOLueTOTgIIE5FJAcotOPJ6PiTyGVGijicrzEpdsTAQIDAQAB");

    private PrivateKey privateKey = RSAUtil.string2PrivateKey("MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQDF12DUwmuyNf/aAKTY833J/KQ4GsIHaKMzCaynPAViG0/hrMMV6vsJnTc2hPpavuD7rGbH0+UVpd+6FIDSkf2xgiOnMDPmiS+3xZm0NOMrvEGkvL9Ie+GE3bg0dA0n2cXu3Vfe+H5bhfHmswfMr0LitDI8dUetBmxZS+CcIwf7zVCip5/NANtCcV0e1ZUKj683jWA8vfZZbbsrOLfLUeidGTA3hbzvxISLWhj8aNkgQA3mFfct1q6yJLPLxKGS7o3IvlOmn4I4MyIm9OI2CngM5O/+PMOP7GUE5EyMiiqrzYQ4u55M5OAggTkUkByi048no+JPIZUaKOJyvMSl2xMBAgMBAAECggEAFOVcutwWF+qQLWxn/Ooq3QysI6jf2Xx50EgzTi6Ouv/7ffCLYmNBRJCwZjfBYZhLlwlsiaqdxZ2Rdcv4l0UIMJl4Po5ezPisa5KZW2lCnTnzDP1KjtNWzHvGmvOu+AiOWV5Ti59faTdKh31dU4cnX15899nDSkklZIEZJiSlETk8v90ixA6HS94IjOAPnb5FuqLdnjoAV1hZX3cik77cDP4QxlS+FBBDu4A3XnogO7rKB2Dw5LUNkXhRfaVgrND5Vcs3tbHUuSoRro6+b7HYmzkgzBp50s0yx338KsRs5xi7JpkdKZEf6f6z/M00wmEl11gbW5vQBhPvUx19m/wQKQKBgQDmDFWJWLuI8ERkp0pJ0PfnIJbRs4wqB5hJSvIMrhfJxHGihY91/18rmubL0cYcwpjhMtLwQpttnm9D2wWWW5YbqsU6xgM6Ut77RXRa8fKx10phlxIDFAy+HGpYjrPR6jphWtSAD98fXBelLIMn76gTmYTuqb3fbzD7OKldA3JdXwKBgQDcKO14Ht1r+RH1B+e8CC/gnPgstghbBrtO0pLHaTZIIBo2B49MwKyowiYfIiXtVl3O519rmd6jI1HqgVHbR0Lqbt9f//1PZ1Vgxh5WW4es34QRzALP+D+aTuR6b5Oj/5Kb+cdSNkfmFEYl740VQr5MkWCbyKDk3OXNq1z5qjALnwKBgAsNuqpvzgsFaHPhqeskFFVRto168Bgw5ztWU40SbGgwfTlO65bC34LL3Z0bhkZpf0kK1SHgQXGh9DyxcHJKEktgCDwLuan6w2YVb0LTb1LmQlewF5h4RSh5Ww7IRJGojIeTArTojlMGbMht/BtWI6BVdxK0qz/qKtNXZKPof31HAoGAcPlXklJUUTnIazKZSbXxy5MBDwCCHSPBLluEEmv8/NnsWVlbwYkFkLvAEC3XxIiTLr8wCek1hAV13Z5qwNk6j05BteOIuxxNhZFks+/YGW/BN9i2gL3k9WwSdnmXC4r0UrHEBH3yOzCgJotcnFJZkLTxHD/BwG9SRU3wMfWRHgUCgYAlZii9nrb9w1DjEs+Y7XPc3GQjgpjuxjSanZxKOCnUp2h2vJZfe4cfXgfjhAxclFhouypuYCUDjGEGGRcVjQK63nA2OEbjqehTR+2srSOLCzI7hdTojWkDPzL5qSIDbuaFfW9os02FqLHKhWRxNWMfpSRp2Mn7cUx9CsNLYe7+oQ==");

    private Gson gson = new Gson();

    public RSAAuthController() throws Exception {
    }

    @ResponseBody
    @RequestMapping(value = "auth",method = RequestMethod.GET,produces = "application/json")
    public String log(HttpServletRequest request, String data) throws Exception {

        String content = gson.toJson(new RSAAuth());

        byte[] bytes1 = RSAUtil.publicEncrypt(content.getBytes("UTF-8"), publicKey);

        String result = new String(Base64.getEncoder().encode(bytes1), "UTF-8");

        DataWrap wrap = new DataWrap(result);

        return gson.toJson(wrap);

    }

}
