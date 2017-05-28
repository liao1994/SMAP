@echo I4PRJ Rapport characters:
@pdftotext I4PRJRapport.pdf -enc UTF-8 - | wc
@echo   Lines ^| Words ^| Chars
@pause