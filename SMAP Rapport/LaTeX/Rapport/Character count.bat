@echo ShineMyRoom Rapport characters:
@pdftotext ShineMyRoom.pdf -enc UTF-8 - | wc -m
@echo   Lines ^| Words ^| Chars
@pause