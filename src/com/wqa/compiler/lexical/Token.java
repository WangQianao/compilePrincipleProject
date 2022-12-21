package com.wqa.compiler.lexical;

public class Token {

        public int row;
        public String type;
        public String content;
        public Token(int row, String type, String content) {
            this.row = row;
            this.type = type;
            this.content = content;
        }

        public Token(){}

        @Override
        public String toString() {
            return "("+row+","+type+","+content+")";
        }
}
