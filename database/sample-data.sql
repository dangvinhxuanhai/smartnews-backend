USE [SmartNewsDB] GO

-- Disable Foreign Key constraints temporarily to safely insert data
EXEC sp_MSforeachtable "ALTER TABLE ? NOCHECK CONSTRAINT ALL" GO

-- 1. Insert Data for [SystemAccount]

SET IDENTITY_INSERT [dbo].[SystemAccount] ON;
GO


INSERT INTO [dbo].[SystemAccount] ([AccountId], [Email], [Password], [Name], [Role], [Status], [IsVerified], [VerificationToken], [Provider], [ProviderId])
VALUES (1, N'admin@smartnews.com', N'$2a$12$E9xZ1Y8K/9X5gq4J8K1E8e.91L5e51a591238491028391023', N'Quản trị viên', N'Admin', 1, 1, NULL, N'Local', NULL),
       (2, N'editor.nam@smartnews.com', N'$2a$12$E9xZ1Y8K/9X5gq4J8K1E8e.91L5e51a591238491028391023', N'Nguyễn Văn Nam', N'Staff', 1, 1, NULL, N'Local', NULL),
       (3, N'writer.lh@smartnews.com', N'$2a$12$E9xZ1Y8K/9X5gq4J8K1E8e.91L5e51a591238491028391023', N'Lê Thị Hoa', N'Staff', 1, 1, NULL, N'Google', N'google-uid-102938'),
       (4, N'writer.tu@smartnews.com', N'$2a$12$E9xZ1Y8K/9X5gq4J8K1E8e.91L5e51a591238491028391023', N'Trần Anh Tú', N'Staff', 1, 0, N'verify_token_123456', N'Local', NULL);


SET IDENTITY_INSERT [dbo].[SystemAccount] OFF;
GO

-- 2. Insert Data for [Category]

SET IDENTITY_INSERT [dbo].[Category] ON;
GO

-- Parent Categories

INSERT INTO [dbo].[Category] ([CategoryId], [CategoryName], [ParentId], [IsActive])
VALUES (1, N'Công nghệ', NULL, 1),
       (2, N'Kinh tế', NULL, 1),
       (3, N'Giải trí', NULL, 1),
       (4, N'Thể thao', NULL, 1);
	   
-- Sub-Categories

INSERT INTO [dbo].[Category] ([CategoryId], [CategoryName], [ParentId], [IsActive])
VALUES (5, N'Trí tuệ nhân tạo (AI)', 1, 1),
       (6, N'Lập trình & Phần mềm', 1, 1),
       (7, N'Bất động sản', 2, 1),
       (8, N'Tài chính cá nhân', 2, 1),
       (9, N'Điện ảnh', 3, 1),
       (10, N'Bóng đá', 4, 1);


SET IDENTITY_INSERT [dbo].[Category] OFF;
GO

-- 3. Insert Data for [Tag]

SET IDENTITY_INSERT [dbo].[Tag] ON;
GO


INSERT INTO [dbo].[Tag] ([TagId], [TagName])
VALUES (1, N'ChatGPT'),
       (2, N'SQL Server'),
       (3, N'dotnet'),
       (4, N'Bất Động Sản 2026'),
       (5, N'Ngoại Hạng Anh'),
       (6, N'Phim Hót'),
       (7, N'Xu Hướng');


SET IDENTITY_INSERT [dbo].[Tag] OFF;
GO

-- 4. Insert Data for [NewsArticle]

SET IDENTITY_INSERT [dbo].[NewsArticle] ON;
GO


INSERT INTO [dbo].[NewsArticle] ([ArticleId], [Title], [Content], [CreatedDate], [UpdatedDate], [Status], [CategoryId], [AuthorId], [ViewCount], [ImageUrl])
VALUES (1, N'Đột phá mới trong công nghệ AI năm 2026', N'Cùng tìm hiểu các công nghệ trí tuệ nhân tạo mới nhất đang thay đổi hoàn toàn cách chúng ta làm việc và sáng tạo trong năm 2026...', DATEADD(DAY, -10, GETDATE()), DATEADD(DAY, -8, GETDATE()), N'Published', 5, 2, 1250, N'https://images.unsplash.com/photo-1677442136019-21780efad99a'),
       (2, N'Tối ưu hóa truy vấn trong SQL Server 2022', N'Hướng dẫn chi tiết cách viết truy vấn và đánh chỉ mục (Index) hiệu quả cho các ứng dụng web quy mô lớn...', DATEADD(DAY, -5, GETDATE()), DATEADD(DAY, -2, GETDATE()), N'Published', 6, 2, 840, N'https://images.unsplash.com/photo-1544383835-bda2bc66a55d'),
       (3, N'Dự báo thị trường Bất động sản quý IV/2026', N'Báo cáo từ các chuyên gia hàng đầu về xu hướng giá nhà đất và phân khúc căn hộ cao cấp...', DATEADD(DAY, -3, GETDATE()), GETDATE(), N'Published', 7, 3, 420, N'https://images.unsplash.com/photo-1560518883-ce09059eeffa'),
       (4, N'Tổng hợp vòng đấu mới nhất giải Ngoại Hạng Anh', N'Những diễn biến kịch tính và các bàn thắng đẹp mắt trong loạt trận rạng sáng nay...', DATEADD(DAY, -1, GETDATE()), NULL, N'Pending', 10, 3, 105, N'https://images.unsplash.com/photo-1508098682722-e99c43a406b2'),
       (5, N'Bản nháp: Đánh giá phim điện ảnh mới chiếu rạp', N'Nội dung đánh giá chi tiết bộ phim bom tấn mới ra mắt...', GETDATE(), NULL, N'Draft', 9, 4, 0, N'https://images.unsplash.com/photo-1489599849927-2ee91cede3ba');


SET IDENTITY_INSERT [dbo].[NewsArticle] OFF;
GO

-- 5. Insert Data for [ArticleTag]

INSERT INTO [dbo].[ArticleTag] ([ArticleId], [TagId])
VALUES 
(1, 1),-- Bài 1 - Tag: ChatGPT
(1, 7),-- Bài 1 - Tag: Xu Hướng
(2, 2),-- Bài 2 - Tag: SQL Server
(2, 3),-- Bài 2 - Tag: dotnet
(3, 4),-- Bài 3 - Tag: Bất Động Sản 2026
(4, 5),-- Bài 4 - Tag: Ngoại Hạng Anh
(5, 6);-- Bài 5 - Tag: Phim Hót

GO-- Re-enable Foreign Key constraints

EXEC sp_MSforeachtable "ALTER TABLE ? WITH CHECK CHECK CONSTRAINT ALL" GO