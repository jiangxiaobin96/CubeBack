package com.example.demo.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.controller.Search;

@RestController
public class Operation {

    Search search = new Search();

    public static String findShorterSolutions(String scrambledCube) {
        //Find shorter solutions (try more probes even a solution has already been found)
        //In this example, we try AT LEAST 10000 phase2 probes to find shorter solutions.
        String result = new Search().solution(scrambledCube, 21, 100000000, 10000, 0);
        return result;
//        System.out.println(result);
        // L2 U  D2 R' B  U2 L  F  U  R2 D2 F2 U' L2 U  B  D  R'
    }

    public static String outputControl(String scrambledCube) {
        String result = new Search().solution(scrambledCube, 21, 100000000, 0, Search.APPEND_LENGTH);
        return result;
        //        System.out.println(result);
        // R2 U2 B2 L2 F2 U' L2 R2 B2 R2 D  B2 F  L' F  U2 F' R' D' L2 R' (21f)

//        result = new Search().solution(scrambledCube, 21, 100000000, 0, Search.USE_SEPARATOR | Search.INVERSE_SOLUTION);
//        System.out.println(result);
        // R  L2 D  R  F  U2 F' L  F' .  B2 D' R2 B2 R2 L2 U  F2 L2 B2 U2 R2
    }
    @RequestMapping(value = "/operation",method = RequestMethod.POST)
    public String getOperation(String initC){
        // Full initialization, which will take about 200ms.
        // The solver will be about 5x~10x slower without full initialization.
        long startTime = System.nanoTime();
        Search.init();
        System.out.println("Init time: " + (System.nanoTime() - startTime) / 1.0E6 + " ms");

        /** prepare scrambledCube as
         *
         *             |************|
         *             |*U1**U2**U3*|
         *             |************|
         *             |*U4**U5**U6*|
         *             |************|
         *             |*U7**U8**U9*|
         *             |************|
         * ************|************|************|************|
         * *L1**L2**L3*|*F1**F2**F3*|*R1**R2**R3*|*B1**B2**B3*|
         * ************|************|************|************|
         * *L4**L5**L6*|*F4**F5**F6*|*R4**R5**R6*|*B4**B5**B6*|
         * ************|************|************|************|
         * *L7**L8**L9*|*F7**F8**F9*|*R7**R8**R9*|*B7**B8**B9*|
         * ************|************|************|************|
         *             |************|
         *             |*D1**D2**D3*|
         *             |************|
         *             |*D4**D5**D6*|
         *             |************|
         *             |*D7**D8**D9*|
         *             |************|
         *
         * -> U1 U2 ... U9 R1 ... R9 F1 ... F9 D1 ... D9 L1 ... L9 B1 ... B9
         */
//        String scrambledCube = "DRLUUBFBRBLURRLRUBLRDDFDLFUFUFFDBRDUBRUFLLFDDBFLUBLRBD";
        // scrambledCube can also be optained by specific moves
//        scrambledCube = Tools.fromScramble("R L2 D R F U2 F' L F' B2 D' R2 B2 R2 L2 U F2 L2 B2 U2 R2");
//        System.out.println(scrambledCube);

//        simpleSolve(scrambledCube);
//        String res = outputControl(scrambledCube);
        String result = "";
//        int check = search.verify(initC);
//        System.out.println("check:"+check);
        String res = findShorterSolutions(initC);
        System.out.println(res);
        if (res.contains("Error")) {
            System.out.println("进入");
            switch (res.charAt(res.length() - 1)) {
                case '1':
                    result = "Error:There are not exactly nine facelets of each color!";
//                    break;
                case '2':
                    result = "Error:Not all 12 edges exist exactly once!";
//                    break;
                case '3':
                    result = "Error:Flip error One edge has to be flipped!";
//                    break;
                case '4':
                    result = "Error:Not all 8 corners exist exactly once!";
//                    break;
                case '5':
                    result = "Error:Twist error One corner has to be twisted!";
//                    break;
                case '6':
                    result = "Error:Parity error Two corners or two edges have to be exchanged!";
//                    break;
                case '7':
                    result = "Error:No solution exists for the given maximum move number!";
//                    break;
                case '8':
                    result = "Error:Timeout, no solution found within given maximum time!";
//                    break;
            }
            System.out.println(result);
        }else{
            res = res.toLowerCase();

            char[] c = res.toCharArray();

            for(int i=0;i<c.length;i++){
                if(c[i] <= 'z' && c[i] >= 'a') result += c[i];
                if(c[i] == '\'') result += c[i];
                if(c[i] == '2') result += c[i-1];
            }
//            System.out.println("res:"+res);
            System.out.println("result:"+result);
        }

//        findShorterSolutions(scrambledCube);
//        continueSearch(scrambledCube);
        return result;
    }

    @RequestMapping(value = "/scramble",method = RequestMethod.GET)
    public String scrambleCube(){
        String r = Tools.randomCube();
        System.out.println(r);
        return r;
    }
}
