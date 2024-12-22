package com.example.core.enums;

public enum ProjectClassificationEnum {
    // 框架（例如React, Vue这种）
    FRAMEWORK("Framework", "A reusable design for a software system or application, such as React or Vue."),

    // 库或模块
    LIBRARY_OR_MODULE("Library/Module", "A collection of code and subroutines used to perform tasks or implement abstract data types."),

    // 大模型或机器学习
    LARGE_MODEL_OR_MACHINE_LEARNING("Large Model/Machine Learning", "Refers to large-scale machine learning models used for tasks such as prediction, classification, and more."),

    // 开源操作系统和系统工具
    OPEN_SOURCE_OS_AND_TOOLS("Open Source OS and Tools", "Operating systems and tools that are made available with an open source license."),

    // 前端应用
    FRONTEND_APPLICATION("Frontend Application", "Software that runs on the user's side, usually in a web browser, and handles user interactions."),

    // 后端服务
    BACKEND_SERVICE("Backend Service", "Software that runs on a server and handles data storage, retrieval, and application logic."),

    // Web框架
    WEB_FRAMEWORK("Web Framework", "A software framework that is designed to support the development of web applications or web services."),

    // 游戏开发
    GAME_DEVELOPMENT("Game Development", "The process of creating a game, including design, programming, art, audio, and testing."),

    // 开源软件开发
    OPEN_SOURCE_SOFTWARE_DEVELOPMENT("Open Source Software Development", "The practice of creating software where the source code is freely available and may be modified and distributed by anyone."),

    // 脚本语言
    SCRIPTING_LANGUAGE("Scripting Language", "A programming language that supports scripts, which are small pieces of source code that are usually interpreted or run by another program rather than being compiled into machine code.");

    private final String term;
    private final String description;

    ProjectClassificationEnum(String term, String description) {
        this.term = term;
        this.description = description;
    }

    public String getTerm() {
        return term;
    }

    public String getDescription() {
        return description;
    }
}
