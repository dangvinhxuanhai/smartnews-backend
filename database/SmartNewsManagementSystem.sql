CREATE DATABASE SmartNewsDB;
GO

USE SmartNewsDB;
GO

CREATE TABLE SystemAccount (
    AccountId INT IDENTITY PRIMARY KEY,
    Email NVARCHAR(100) UNIQUE NOT NULL,
    Password NVARCHAR(255) NOT NULL,
    Name NVARCHAR(100),
    Role NVARCHAR(20) CHECK (Role IN ('Admin', 'Staff')),
    Status BIT DEFAULT 1
);

CREATE TABLE Category (
    CategoryId INT IDENTITY PRIMARY KEY,
    CategoryName NVARCHAR(100),
    ParentId INT NULL,
    IsActive BIT DEFAULT 1,
    FOREIGN KEY (ParentId) REFERENCES Category(CategoryId)
);

CREATE TABLE NewsArticle (
    ArticleId INT IDENTITY PRIMARY KEY,
    Title NVARCHAR(255),
    Content NVARCHAR(MAX),
    CreatedDate DATETIME DEFAULT GETDATE(),
    UpdatedDate DATETIME NULL,
    Status NVARCHAR(20) CHECK (Status IN ('Draft', 'Published')),
    CategoryId INT,
    AuthorId INT,
    ViewCount INT DEFAULT 0,
    FOREIGN KEY (CategoryId) REFERENCES Category(CategoryId),
    FOREIGN KEY (AuthorId) REFERENCES SystemAccount(AccountId)
);

CREATE TABLE Tag (
    TagId INT IDENTITY PRIMARY KEY,
    TagName NVARCHAR(50)
);

CREATE TABLE ArticleTag (
    ArticleId INT,
    TagId INT,
    PRIMARY KEY (ArticleId, TagId),
    FOREIGN KEY (ArticleId) REFERENCES NewsArticle(ArticleId),
    FOREIGN KEY (TagId) REFERENCES Tag(TagId)
);
/* SmartNews multi-source schema revised after profiling UVN-1 */

CREATE TABLE ExternalSource (
    SourceId INT IDENTITY PRIMARY KEY,
    Code NVARCHAR(50) NOT NULL,
    Name NVARCHAR(150) NOT NULL,
    WebsiteUrl NVARCHAR(500) NULL,
    RssUrl NVARCHAR(500) NULL,
    LogoUrl NVARCHAR(500) NULL,
    CountryCode NVARCHAR(10) NULL,
    LanguageCode NVARCHAR(10) NULL,
    IsActive BIT NOT NULL DEFAULT 1,
    CreatedDate DATETIME2 NOT NULL DEFAULT SYSDATETIME(),

    CONSTRAINT UQ_ExternalSource_Code UNIQUE (Code),
    CONSTRAINT UQ_ExternalSource_Name UNIQUE (Name)
);

CREATE TABLE ExternalArticle (
    ExternalArticleId INT IDENTITY PRIMARY KEY,
    SourceId INT NOT NULL,

    SourceArticleKey NVARCHAR(150) NULL,

    -- Raw category from the original publisher/dataset.
    OriginalCategory NVARCHAR(100) NULL,

    -- Optional normalized SmartNews category.
    CategoryId INT NULL,

    Title NVARCHAR(500) NOT NULL,
    Summary NVARCHAR(MAX) NULL,
    Content NVARCHAR(MAX) NULL,

    Url NVARCHAR(700) NOT NULL,
    ImageUrl NVARCHAR(700) NULL,
    Author NVARCHAR(255) NULL,

    PublishedAt DATETIME2 NULL,
    ImportedAt DATETIME2 NOT NULL DEFAULT SYSDATETIME(),

    CONSTRAINT FK_ExternalArticle_Source
        FOREIGN KEY (SourceId) REFERENCES ExternalSource(SourceId),

    CONSTRAINT FK_ExternalArticle_Category
        FOREIGN KEY (CategoryId) REFERENCES Category(CategoryId),

    CONSTRAINT UQ_ExternalArticle_Url UNIQUE (Url),
    CONSTRAINT UQ_ExternalArticle_SourceKey UNIQUE (SourceId, SourceArticleKey)
);

CREATE TABLE Story (
    StoryId INT IDENTITY PRIMARY KEY,
    Title NVARCHAR(255) NOT NULL,
    Summary NVARCHAR(MAX) NULL,
    HeroImageUrl NVARCHAR(700) NULL,

    Status NVARCHAR(20) NOT NULL DEFAULT 'Draft',
    CategoryId INT NULL,
    CreatedBy INT NULL,

    ViewCount INT NOT NULL DEFAULT 0,
    CreatedDate DATETIME2 NOT NULL DEFAULT SYSDATETIME(),
    UpdatedDate DATETIME2 NULL,
    PublishedDate DATETIME2 NULL,

    CONSTRAINT CK_Story_Status CHECK (Status IN ('Draft', 'Published')),
    CONSTRAINT FK_Story_Category FOREIGN KEY (CategoryId) REFERENCES Category(CategoryId),
    CONSTRAINT FK_Story_CreatedBy FOREIGN KEY (CreatedBy) REFERENCES SystemAccount(AccountId)
);

CREATE TABLE StoryExternalArticle (
    StoryId INT NOT NULL,
    ExternalArticleId INT NOT NULL,
    AddedDate DATETIME2 NOT NULL DEFAULT SYSDATETIME(),

    PRIMARY KEY (StoryId, ExternalArticleId),
    CONSTRAINT FK_StoryExternalArticle_Story
        FOREIGN KEY (StoryId) REFERENCES Story(StoryId) ON DELETE CASCADE,
    CONSTRAINT FK_StoryExternalArticle_ExternalArticle
        FOREIGN KEY (ExternalArticleId) REFERENCES ExternalArticle(ExternalArticleId) ON DELETE CASCADE
);

CREATE TABLE StoryNewsArticle (
    StoryId INT NOT NULL,
    ArticleId INT NOT NULL,
    AddedDate DATETIME2 NOT NULL DEFAULT SYSDATETIME(),

    PRIMARY KEY (StoryId, ArticleId),
    CONSTRAINT FK_StoryNewsArticle_Story
        FOREIGN KEY (StoryId) REFERENCES Story(StoryId) ON DELETE CASCADE,
    CONSTRAINT FK_StoryNewsArticle_NewsArticle
        FOREIGN KEY (ArticleId) REFERENCES NewsArticle(ArticleId) ON DELETE CASCADE
);

CREATE TABLE StoryTag (
    StoryId INT NOT NULL,
    TagId INT NOT NULL,

    PRIMARY KEY (StoryId, TagId),
    CONSTRAINT FK_StoryTag_Story
        FOREIGN KEY (StoryId) REFERENCES Story(StoryId) ON DELETE CASCADE,
    CONSTRAINT FK_StoryTag_Tag
        FOREIGN KEY (TagId) REFERENCES Tag(TagId) ON DELETE CASCADE
);

CREATE INDEX IX_ExternalArticle_SourceId ON ExternalArticle(SourceId);
CREATE INDEX IX_ExternalArticle_CategoryId ON ExternalArticle(CategoryId);
CREATE INDEX IX_ExternalArticle_PublishedAt ON ExternalArticle(PublishedAt DESC);
CREATE INDEX IX_Story_Status ON Story(Status);
CREATE INDEX IX_Story_CategoryId ON Story(CategoryId);
CREATE INDEX IX_Story_CreatedDate ON Story(CreatedDate DESC);