@echo off
setlocal
cd /d "%~dp0"

set "PRINTER=HP LaserJet MFP M129-M134"
set "PORT=8787"
set "TASK_NAME=PreAssemblyPrintBridge"
set "BRIDGE_PS1=%~dp0PreAssemblyPrintBridge.ps1"
set "BRIDGE_B64=%TEMP%\PreAssemblyPrintBridge_%RANDOM%.b64"

net session >nul 2>&1
if not "%errorlevel%"=="0" (
    echo Requesting administrator rights...
    powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "Start-Process -FilePath '%~f0' -Verb RunAs"
    exit /b
)

if not exist "%BRIDGE_PS1%" (
    echo Creating PreAssemblyPrintBridge.ps1 near this BAT...
    > "%BRIDGE_B64%" (
        echo(cGFyYW0oCiAgICBbc3RyaW5nXSRQcmludGVyTmFtZSA9ICJIUCBMYXNlckpldCBNRlAgTTEyOS1N
        echo(MTM0IiwKICAgIFtpbnRdJFBvcnQgPSA4Nzg3LAogICAgW3N3aXRjaF0kQWxsb3dEZWZhdWx0UHJp
        echo(bnRlckZhbGxiYWNrCikKCiRFcnJvckFjdGlvblByZWZlcmVuY2UgPSAiU3RvcCIKJExvZ1BhdGgg
        echo(PSBKb2luLVBhdGggJFBTU2NyaXB0Um9vdCAiUHJlQXNzZW1ibHlQcmludEJyaWRnZS5sb2ciCgpm
        echo(dW5jdGlvbiBXcml0ZS1CcmlkZ2VMb2cgewogICAgcGFyYW0oW3N0cmluZ10kTWVzc2FnZSkKICAg
        echo(ICRsaW5lID0gIiQoR2V0LURhdGUgLUZvcm1hdCAneXl5eS1NTS1kZCBISDptbTpzcycpICRNZXNz
        echo(YWdlIgogICAgV3JpdGUtSG9zdCAkbGluZQogICAgQWRkLUNvbnRlbnQgLUxpdGVyYWxQYXRoICRM
        echo(b2dQYXRoIC1WYWx1ZSAkbGluZSAtRW5jb2RpbmcgVVRGOAp9CgpmdW5jdGlvbiBGaW5kLUhlYWRl
        echo(ckVuZCB7CiAgICBwYXJhbShbYnl0ZVtdXSRCeXRlcywgW2ludF0kQ291bnQpCiAgICBmb3IgKCRp
        echo(ID0gMDsgJGkgLWxlICRDb3VudCAtIDQ7ICRpKyspIHsKICAgICAgICBpZiAoJEJ5dGVzWyRpXSAt
        echo(ZXEgMTMgLWFuZCAkQnl0ZXNbJGkgKyAxXSAtZXEgMTAgLWFuZCAkQnl0ZXNbJGkgKyAyXSAtZXEg
        echo(MTMgLWFuZCAkQnl0ZXNbJGkgKyAzXSAtZXEgMTApIHsKICAgICAgICAgICAgcmV0dXJuICRpCiAg
        echo(ICAgICAgfQogICAgfQogICAgcmV0dXJuIC0xCn0KCmZ1bmN0aW9uIFJlYWQtSHR0cFJlcXVlc3Qg
        echo(ewogICAgcGFyYW0oW1N5c3RlbS5OZXQuU29ja2V0cy5OZXR3b3JrU3RyZWFtXSRTdHJlYW0pCgog
        echo(ICAgJGJ1ZmZlciA9IE5ldy1PYmplY3QgYnl0ZVtdIDQwOTYKICAgICRzdG9yYWdlID0gTmV3LU9i
        echo(amVjdCAiU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuTGlzdFtieXRlXSIKICAgICRoZWFkZXJF
        echo(bmQgPSAtMQoKICAgIHdoaWxlICgkaGVhZGVyRW5kIC1sdCAwKSB7CiAgICAgICAgJHJlYWQgPSAk
        echo(U3RyZWFtLlJlYWQoJGJ1ZmZlciwgMCwgJGJ1ZmZlci5MZW5ndGgpCiAgICAgICAgaWYgKCRyZWFk
        echo(IC1sZSAwKSB7CiAgICAgICAgICAgIHRocm93ICJFbXB0eSBIVFRQIHJlcXVlc3QiCiAgICAgICAg
        echo(fQogICAgICAgIGZvciAoJGkgPSAwOyAkaSAtbHQgJHJlYWQ7ICRpKyspIHsKICAgICAgICAgICAg
        echo(JHN0b3JhZ2UuQWRkKCRidWZmZXJbJGldKQogICAgICAgIH0KICAgICAgICAkYnl0ZXMgPSAkc3Rv
        echo(cmFnZS5Ub0FycmF5KCkKICAgICAgICAkaGVhZGVyRW5kID0gRmluZC1IZWFkZXJFbmQgLUJ5dGVz
        echo(ICRieXRlcyAtQ291bnQgJGJ5dGVzLkxlbmd0aAogICAgfQoKICAgICRieXRlcyA9ICRzdG9yYWdl
        echo(LlRvQXJyYXkoKQogICAgJGhlYWRlclRleHQgPSBbU3lzdGVtLlRleHQuRW5jb2RpbmddOjpBU0NJ
        echo(SS5HZXRTdHJpbmcoJGJ5dGVzLCAwLCAkaGVhZGVyRW5kKQogICAgJGhlYWRlckxpbmVzID0gJGhl
        echo(YWRlclRleHQgLXNwbGl0ICJgcmBuIgogICAgJGZpcnN0TGluZSA9ICRoZWFkZXJMaW5lc1swXQog
        echo(ICAgJGNvbnRlbnRMZW5ndGggPSAwCiAgICBmb3JlYWNoICgkbGluZSBpbiAkaGVhZGVyTGluZXMp
        echo(IHsKICAgICAgICBpZiAoJGxpbmUgLW1hdGNoICdeQ29udGVudC1MZW5ndGg6XHMqKFxkKyknKSB7
        echo(CiAgICAgICAgICAgICRjb250ZW50TGVuZ3RoID0gW2ludF0kbWF0Y2hlc1sxXQogICAgICAgIH0K
        echo(ICAgIH0KCiAgICAkYm9keVN0YXJ0ID0gJGhlYWRlckVuZCArIDQKICAgICRhdmFpbGFibGUgPSBb
        echo(TWF0aF06Ok1heCgwLCAkYnl0ZXMuTGVuZ3RoIC0gJGJvZHlTdGFydCkKICAgICRib2R5Qnl0ZXMg
        echo(PSBOZXctT2JqZWN0IGJ5dGVbXSAkY29udGVudExlbmd0aAogICAgJG9mZnNldCA9IDAKICAgIGlm
        echo(ICgkYXZhaWxhYmxlIC1ndCAwIC1hbmQgJGNvbnRlbnRMZW5ndGggLWd0IDApIHsKICAgICAgICAk
        echo(dG9Db3B5ID0gW01hdGhdOjpNaW4oJGF2YWlsYWJsZSwgJGNvbnRlbnRMZW5ndGgpCiAgICAgICAg
        echo(W0FycmF5XTo6Q29weSgkYnl0ZXMsICRib2R5U3RhcnQsICRib2R5Qnl0ZXMsIDAsICR0b0NvcHkp
        echo(CiAgICAgICAgJG9mZnNldCA9ICR0b0NvcHkKICAgIH0KICAgIHdoaWxlICgkb2Zmc2V0IC1sdCAk
        echo(Y29udGVudExlbmd0aCkgewogICAgICAgICRyZWFkID0gJFN0cmVhbS5SZWFkKCRib2R5Qnl0ZXMs
        echo(ICRvZmZzZXQsICRjb250ZW50TGVuZ3RoIC0gJG9mZnNldCkKICAgICAgICBpZiAoJHJlYWQgLWxl
        echo(IDApIHsKICAgICAgICAgICAgYnJlYWsKICAgICAgICB9CiAgICAgICAgJG9mZnNldCArPSAkcmVh
        echo(ZAogICAgfQoKICAgIFtwc2N1c3RvbW9iamVjdF1AewogICAgICAgIEZpcnN0TGluZSA9ICRmaXJz
        echo(dExpbmUKICAgICAgICBCb2R5ID0gW1N5c3RlbS5UZXh0LkVuY29kaW5nXTo6VVRGOC5HZXRTdHJp
        echo(bmcoJGJvZHlCeXRlcywgMCwgJG9mZnNldCkKICAgIH0KfQoKZnVuY3Rpb24gU2VuZC1IdHRwUmVz
        echo(cG9uc2UgewogICAgcGFyYW0oCiAgICAgICAgW1N5c3RlbS5OZXQuU29ja2V0cy5OZXR3b3JrU3Ry
        echo(ZWFtXSRTdHJlYW0sCiAgICAgICAgW2ludF0kQ29kZSwKICAgICAgICBbc3RyaW5nXSRTdGF0dXMs
        echo(CiAgICAgICAgW3N0cmluZ10kQm9keQogICAgKQoKICAgICRib2R5Qnl0ZXMgPSBbU3lzdGVtLlRl
        echo(eHQuRW5jb2RpbmddOjpVVEY4LkdldEJ5dGVzKCRCb2R5KQogICAgJGhlYWRlciA9ICJIVFRQLzEu
        echo(MSAkQ29kZSAkU3RhdHVzYHJgbkNvbnRlbnQtVHlwZTogdGV4dC9wbGFpbjsgY2hhcnNldD11dGYt
        echo(OGByYG5Db250ZW50LUxlbmd0aDogJCgkYm9keUJ5dGVzLkxlbmd0aClgcmBuQ29ubmVjdGlvbjog
        echo(Y2xvc2VgcmBuYHJgbiIKICAgICRoZWFkZXJCeXRlcyA9IFtTeXN0ZW0uVGV4dC5FbmNvZGluZ106
        echo(OkFTQ0lJLkdldEJ5dGVzKCRoZWFkZXIpCiAgICAkU3RyZWFtLldyaXRlKCRoZWFkZXJCeXRlcywg
        echo(MCwgJGhlYWRlckJ5dGVzLkxlbmd0aCkKICAgICRTdHJlYW0uV3JpdGUoJGJvZHlCeXRlcywgMCwg
        echo(JGJvZHlCeXRlcy5MZW5ndGgpCiAgICAkU3RyZWFtLkZsdXNoKCkKfQoKZnVuY3Rpb24gU3BsaXQt
        echo(UHJpbnRhYmxlTGluZXMgewogICAgcGFyYW0oW3N0cmluZ10kVGV4dCwgW2ludF0kTWF4Q2hhcnMg
        echo(PSA5NSkKICAgICRyZXN1bHQgPSBOZXctT2JqZWN0ICJTeXN0ZW0uQ29sbGVjdGlvbnMuR2VuZXJp
        echo(Yy5MaXN0W3N0cmluZ10iCiAgICBmb3JlYWNoICgkbGluZSBpbiAoJFRleHQgLXNwbGl0ICJgcj9g
        echo(biIpKSB7CiAgICAgICAgaWYgKCRsaW5lLkxlbmd0aCAtbGUgJE1heENoYXJzKSB7CiAgICAgICAg
        echo(ICAgICRyZXN1bHQuQWRkKCRsaW5lKQogICAgICAgICAgICBjb250aW51ZQogICAgICAgIH0KICAg
        echo(ICAgICBmb3IgKCRpID0gMDsgJGkgLWx0ICRsaW5lLkxlbmd0aDsgJGkgKz0gJE1heENoYXJzKSB7
        echo(CiAgICAgICAgICAgICRyZXN1bHQuQWRkKCRsaW5lLlN1YnN0cmluZygkaSwgW01hdGhdOjpNaW4o
        echo(JE1heENoYXJzLCAkbGluZS5MZW5ndGggLSAkaSkpKQogICAgICAgIH0KICAgIH0KICAgIHJldHVy
        echo(biAkcmVzdWx0LlRvQXJyYXkoKQp9CgpmdW5jdGlvbiBHZXQtQXZhaWxhYmxlUHJpbnRlcnNUZXh0
        echo(IHsKICAgIHJldHVybiAoR2V0LVByaW50ZXIgfCBGb3JFYWNoLU9iamVjdCB7CiAgICAgICAgaWYg
        echo(KCRfLlNoYXJlTmFtZSkgeyAiJCgkXy5OYW1lKSBbc2hhcmU6ICQoJF8uU2hhcmVOYW1lKV0iIH0g
        echo(ZWxzZSB7ICRfLk5hbWUgfQogICAgfSkgLWpvaW4gIjsgIgp9CgpmdW5jdGlvbiBSZXNvbHZlLVBy
        echo(aW50ZXIgewogICAgcGFyYW0oCiAgICAgICAgW3N0cmluZ10kUHJpbnRlciwKICAgICAgICBbYm9v
        echo(bF0kQWxsb3dGYWxsYmFjayA9ICRmYWxzZQogICAgKQoKICAgICRwcmludGVySW5mbyA9IEdldC1Q
        echo(cmludGVyIC1OYW1lICRQcmludGVyIC1FcnJvckFjdGlvbiBTaWxlbnRseUNvbnRpbnVlCiAgICBp
        echo(ZiAoLW5vdCAkcHJpbnRlckluZm8pIHsKICAgICAgICAkcHJpbnRlckluZm8gPSBHZXQtUHJpbnRl
        echo(ciB8IFdoZXJlLU9iamVjdCB7ICRfLlNoYXJlTmFtZSAtZXEgJFByaW50ZXIgfSB8IFNlbGVjdC1P
        echo(YmplY3QgLUZpcnN0IDEKICAgIH0KICAgIGlmICgtbm90ICRwcmludGVySW5mbyAtYW5kICRBbGxv
        echo(d0ZhbGxiYWNrKSB7CiAgICAgICAgJGRlZmF1bHRQcmludGVyID0gR2V0LUNpbUluc3RhbmNlIFdp
        echo(bjMyX1ByaW50ZXIgfCBXaGVyZS1PYmplY3QgeyAkXy5EZWZhdWx0IH0gfCBTZWxlY3QtT2JqZWN0
        echo(IC1GaXJzdCAxCiAgICAgICAgaWYgKCRkZWZhdWx0UHJpbnRlcikgewogICAgICAgICAgICAkcHJp
        echo(bnRlckluZm8gPSBHZXQtUHJpbnRlciAtTmFtZSAkZGVmYXVsdFByaW50ZXIuTmFtZSAtRXJyb3JB
        echo(Y3Rpb24gU2lsZW50bHlDb250aW51ZQogICAgICAgICAgICBpZiAoJHByaW50ZXJJbmZvKSB7CiAg
        echo(ICAgICAgICAgICAgICBXcml0ZS1CcmlkZ2VMb2cgInByaW50ZXIgJyRQcmludGVyJyB3YXMgbm90
        echo(IGZvdW5kLCB1c2luZyBkZWZhdWx0IHByaW50ZXIgJyQoJHByaW50ZXJJbmZvLk5hbWUpJyIKICAg
        echo(ICAgICAgICAgfQogICAgICAgIH0KICAgIH0KICAgIGlmICgtbm90ICRwcmludGVySW5mbykgewog
        echo(ICAgICAgIHRocm93ICJQcmludGVyIG9yIHNoYXJlICckUHJpbnRlcicgd2FzIG5vdCBmb3VuZCBp
        echo(biBXaW5kb3dzLiBBdmFpbGFibGU6ICQoR2V0LUF2YWlsYWJsZVByaW50ZXJzVGV4dCkuIFNldCB0
        echo(aGUgZXhhY3QgV2luZG93cyBwcmludGVyIG5hbWUgaW4gdGhlIEFuZHJvaWQgYXBwIG9yIHN0YXJ0
        echo(IGJyaWRnZSB3aXRoIC1QcmludGVyTmFtZS4iCiAgICB9CgogICAgcmV0dXJuICRwcmludGVySW5m
        echo(bwp9CgpmdW5jdGlvbiBQcmludC1UZXh0IHsKICAgIHBhcmFtKAogICAgICAgIFtzdHJpbmddJFBy
        echo(aW50ZXIsCiAgICAgICAgW3N0cmluZ10kVGl0bGUsCiAgICAgICAgW3N0cmluZ10kVGV4dAogICAg
        echo(KQoKICAgICRwcmludGVySW5mbyA9IFJlc29sdmUtUHJpbnRlciAtUHJpbnRlciAkUHJpbnRlciAt
        echo(QWxsb3dGYWxsYmFjazokQWxsb3dEZWZhdWx0UHJpbnRlckZhbGxiYWNrLklzUHJlc2VudAoKICAg
        echo(ICRwcmludExpbmVzID0gTmV3LU9iamVjdCAiU3lzdGVtLkNvbGxlY3Rpb25zLkdlbmVyaWMuTGlz
        echo(dFtzdHJpbmddIgogICAgJHByaW50TGluZXMuQWRkKCRUaXRsZSkKICAgICRwcmludExpbmVzLkFk
        echo(ZCgoIj0iICogW01hdGhdOjpNaW4oW01hdGhdOjpNYXgoJFRpdGxlLkxlbmd0aCwgMTIpLCA4MCkp
        echo(KQogICAgJHByaW50TGluZXMuQWRkKCIiKQogICAgZm9yZWFjaCAoJGxpbmUgaW4gKFNwbGl0LVBy
        echo(aW50YWJsZUxpbmVzIC1UZXh0ICRUZXh0KSkgewogICAgICAgICRwcmludExpbmVzLkFkZCgkbGlu
        echo(ZSkKICAgIH0KCiAgICAkcHJpbnRMaW5lcy5Ub0FycmF5KCkgfCBPdXQtUHJpbnRlciAtTmFtZSAk
        echo(cHJpbnRlckluZm8uTmFtZQogICAgcmV0dXJuICRwcmludGVySW5mby5OYW1lCn0KCiRsaXN0ZW5l
        echo(ciA9IFtTeXN0ZW0uTmV0LlNvY2tldHMuVGNwTGlzdGVuZXJdOjpuZXcoW1N5c3RlbS5OZXQuSVBB
        echo(ZGRyZXNzXTo6QW55LCAkUG9ydCkKJGxpc3RlbmVyLlN0YXJ0KCkKV3JpdGUtQnJpZGdlTG9nICJQ
        echo(cmVBc3NlbWJseSBwcmludCBicmlkZ2Ugc3RhcnRlZCBvbiBwb3J0ICRQb3J0LiBQcmludGVyOiAk
        echo(UHJpbnRlck5hbWUiCldyaXRlLUJyaWRnZUxvZyAiTGVhdmUgdGhpcyB3aW5kb3cgb3BlbiB3aGls
        echo(ZSBwcmludGluZyBmcm9tIHRoZSBBbmRyb2lkIGFwcC4iCgp0cnkgewogICAgd2hpbGUgKCR0cnVl
        echo(KSB7CiAgICAgICAgJGNsaWVudCA9ICRsaXN0ZW5lci5BY2NlcHRUY3BDbGllbnQoKQogICAgICAg
        echo(ICRyZW1vdGUgPSAkY2xpZW50LkNsaWVudC5SZW1vdGVFbmRQb2ludC5Ub1N0cmluZygpCiAgICAg
        echo(ICAgV3JpdGUtQnJpZGdlTG9nICJjb25uZWN0aW9uIGZyb20gJHJlbW90ZSIKICAgICAgICB0cnkg
        echo(ewogICAgICAgICAgICAkc3RyZWFtID0gJGNsaWVudC5HZXRTdHJlYW0oKQogICAgICAgICAgICAk
        echo(cmVxdWVzdCA9IFJlYWQtSHR0cFJlcXVlc3QgLVN0cmVhbSAkc3RyZWFtCiAgICAgICAgICAgIFdy
        echo(aXRlLUJyaWRnZUxvZyAicmVxdWVzdDogJCgkcmVxdWVzdC5GaXJzdExpbmUpLCBib2R5IGJ5dGVz
        echo(L2NoYXJzOiAkKCRyZXF1ZXN0LkJvZHkuTGVuZ3RoKSIKICAgICAgICAgICAgaWYgKCRyZXF1ZXN0
        echo(LkZpcnN0TGluZSAtbWF0Y2ggJ15HRVRccysvaGVhbHRoXHMrSFRUUC8nKSB7CiAgICAgICAgICAg
        echo(ICAgICAkcHJpbnRlclRleHQgPSB0cnkgeyAoUmVzb2x2ZS1QcmludGVyIC1QcmludGVyICRQcmlu
        echo(dGVyTmFtZSAtQWxsb3dGYWxsYmFjazokQWxsb3dEZWZhdWx0UHJpbnRlckZhbGxiYWNrLklzUHJl
        echo(c2VudCkuTmFtZSB9IGNhdGNoIHsgIk5PVCBGT1VORDogJFByaW50ZXJOYW1lLiAkKEdldC1BdmFp
        echo(bGFibGVQcmludGVyc1RleHQpIiB9CiAgICAgICAgICAgICAgICBTZW5kLUh0dHBSZXNwb25zZSAt
        echo(U3RyZWFtICRzdHJlYW0gLUNvZGUgMjAwIC1TdGF0dXMgIk9LIiAtQm9keSAiUHJlQXNzZW1ibHlQ
        echo(cmludEJyaWRnZSBPSy4gUHJpbnRlcjogJHByaW50ZXJUZXh0IgogICAgICAgICAgICAgICAgV3Jp
        echo(dGUtQnJpZGdlTG9nICJoZWFsdGggY2hlY2sgZnJvbSAkcmVtb3RlLiBQcmludGVyOiAkcHJpbnRl
        echo(clRleHQiCiAgICAgICAgICAgICAgICBjb250aW51ZQogICAgICAgICAgICB9CgogICAgICAgICAg
        echo(ICBpZiAoJHJlcXVlc3QuRmlyc3RMaW5lIC1ub3RtYXRjaCAnXlBPU1RccysvcHJpbnRccytIVFRQ
        echo(LycpIHsKICAgICAgICAgICAgICAgIFNlbmQtSHR0cFJlc3BvbnNlIC1TdHJlYW0gJHN0cmVhbSAt
        echo(Q29kZSA0MDQgLVN0YXR1cyAiTm90IEZvdW5kIiAtQm9keSAiVXNlIEdFVCAvaGVhbHRoIG9yIFBP
        echo(U1QgL3ByaW50IgogICAgICAgICAgICAgICAgY29udGludWUKICAgICAgICAgICAgfQoKICAgICAg
        echo(ICAgICAgJHBheWxvYWQgPSAkcmVxdWVzdC5Cb2R5IHwgQ29udmVydEZyb20tSnNvbgogICAgICAg
        echo(ICAgICAkcHJpbnRlciA9IGlmICgkcGF5bG9hZC5wcmludGVyKSB7IFtzdHJpbmddJHBheWxvYWQu
        echo(cHJpbnRlciB9IGVsc2UgeyAkUHJpbnRlck5hbWUgfQogICAgICAgICAgICAkdGl0bGUgPSBpZiAo
        echo(JHBheWxvYWQudGl0bGUpIHsgW3N0cmluZ10kcGF5bG9hZC50aXRsZSB9IGVsc2UgeyAiUHJlLWFz
        echo(c2VtYmx5IHByaW50IGpvYiIgfQogICAgICAgICAgICAkdGV4dCA9IFtzdHJpbmddJHBheWxvYWQu
        echo(dGV4dAogICAgICAgICAgICBpZiAoW3N0cmluZ106OklzTnVsbE9yV2hpdGVTcGFjZSgkdGV4dCkp
        echo(IHsKICAgICAgICAgICAgICAgIHRocm93ICJFbXB0eSBwcmludCB0ZXh0IgogICAgICAgICAgICB9
        echo(CiAgICAgICAgICAgIFdyaXRlLUJyaWRnZUxvZyAicHJpbnQgcmVxdWVzdDogdGl0bGU9JyR0aXRs
        echo(ZScsIHJlcXVlc3RlZCBwcmludGVyPSckcHJpbnRlciciCgogICAgICAgICAgICB0cnkgewogICAg
        echo(ICAgICAgICAgICAgJGFjdHVhbFByaW50ZXIgPSBQcmludC1UZXh0IC1QcmludGVyICRwcmludGVy
        echo(IC1UaXRsZSAkdGl0bGUgLVRleHQgJHRleHQKICAgICAgICAgICAgICAgIFNlbmQtSHR0cFJlc3Bv
        echo(bnNlIC1TdHJlYW0gJHN0cmVhbSAtQ29kZSAyMDAgLVN0YXR1cyAiT0siIC1Cb2R5ICJQcmludGVk
        echo(IG9uICRhY3R1YWxQcmludGVyIgogICAgICAgICAgICAgICAgV3JpdGUtQnJpZGdlTG9nICJwcmlu
        echo(dGVkICckdGl0bGUnIG9uICckYWN0dWFsUHJpbnRlciciCiAgICAgICAgICAgIH0gY2F0Y2ggewog
        echo(ICAgICAgICAgICAgICAgJHByaW50RXJyb3IgPSAkXy5FeGNlcHRpb24uTWVzc2FnZQogICAgICAg
        echo(ICAgICAgICAgU2VuZC1IdHRwUmVzcG9uc2UgLVN0cmVhbSAkc3RyZWFtIC1Db2RlIDUwMCAtU3Rh
        echo(dHVzICJQcmludCBFcnJvciIgLUJvZHkgJHByaW50RXJyb3IKICAgICAgICAgICAgICAgIFdyaXRl
        echo(LUJyaWRnZUxvZyAicHJpbnQgZXJyb3I6ICRwcmludEVycm9yIgogICAgICAgICAgICB9CiAgICAg
        echo(ICAgICAgIGNvbnRpbnVlCiAgICAgICAgfSBjYXRjaCB7CiAgICAgICAgICAgICRtZXNzYWdlID0g
        echo(JF8uRXhjZXB0aW9uLk1lc3NhZ2UKICAgICAgICAgICAgdHJ5IHsKICAgICAgICAgICAgICAgIFNl
        echo(bmQtSHR0cFJlc3BvbnNlIC1TdHJlYW0gJHN0cmVhbSAtQ29kZSA1MDAgLVN0YXR1cyAiUHJpbnQg
        echo(RXJyb3IiIC1Cb2R5ICRtZXNzYWdlCiAgICAgICAgICAgIH0gY2F0Y2ggewogICAgICAgICAgICAg
        echo(ICAgIyBDbGllbnQgbWF5IGFscmVhZHkgYmUgY2xvc2VkLgogICAgICAgICAgICB9CiAgICAgICAg
        echo(ICAgIFdyaXRlLUJyaWRnZUxvZyAicHJpbnQgZXJyb3I6ICRtZXNzYWdlIgogICAgICAgIH0gZmlu
        echo(YWxseSB7CiAgICAgICAgICAgICRjbGllbnQuQ2xvc2UoKQogICAgICAgIH0KICAgIH0KfSBmaW5h
        echo(bGx5IHsKICAgICRsaXN0ZW5lci5TdG9wKCkKfQo=
    )
    powershell.exe -NoProfile -ExecutionPolicy Bypass -Command "$b64 = (Get-Content -LiteralPath '%BRIDGE_B64%' -Raw) -replace '\s', ''; $bytes = [Convert]::FromBase64String($b64); [System.IO.File]::WriteAllText('%BRIDGE_PS1%', [System.Text.Encoding]::UTF8.GetString($bytes), [System.Text.UTF8Encoding]::new($false))"
    del "%BRIDGE_B64%" >nul 2>&1
)

echo Configuring PreAssembly print bridge on this computer...
echo Printer: %PRINTER%
echo Port: %PORT%
echo.

powershell.exe -NoProfile -ExecutionPolicy Bypass -Command ^
"$ErrorActionPreference='Stop';" ^
"$script = '%BRIDGE_PS1%';" ^
"$printer = '%PRINTER%';" ^
"$port = %PORT%;" ^
"$taskName = '%TASK_NAME%';" ^
"Write-Host '1/7 Setting active network profile to Private when possible...';" ^
"Get-NetConnectionProfile | Where-Object { $_.IPv4Connectivity -ne 'NoTraffic' -and $_.NetworkCategory -ne 'DomainAuthenticated' } | ForEach-Object { try { Set-NetConnectionProfile -InterfaceIndex $_.InterfaceIndex -NetworkCategory Private -ErrorAction Stop; Write-Host ('   Private: ' + $_.Name) } catch { Write-Host ('   Could not change: ' + $_.Name + ' - ' + $_.Exception.Message) } };" ^
"Write-Host '2/7 Opening firewall for bridge port...';" ^
"$rule = Get-NetFirewallRule -DisplayName 'PreAssembly Print Bridge 8787' -ErrorAction SilentlyContinue;" ^
"if ($rule) { $rule | Set-NetFirewallRule -Enabled True -Profile Any -Action Allow | Out-Null } else { New-NetFirewallRule -DisplayName 'PreAssembly Print Bridge 8787' -Direction Inbound -Action Allow -Protocol TCP -LocalPort $port -Profile Any | Out-Null };" ^
"Write-Host '3/7 Enabling Remote Desktop firewall and service...';" ^
"Set-ItemProperty 'HKLM:\System\CurrentControlSet\Control\Terminal Server' -Name fDenyTSConnections -Value 0;" ^
"Enable-NetFirewallRule -Group '@FirewallAPI.dll,-28752' -ErrorAction SilentlyContinue | Out-Null;" ^
"Set-Service -Name TermService -StartupType Automatic -ErrorAction SilentlyContinue;" ^
"Start-Service -Name TermService -ErrorAction SilentlyContinue;" ^
"try { $rdpGroup = ([System.Security.Principal.SecurityIdentifier]'S-1-5-32-555').Translate([System.Security.Principal.NTAccount]).Value.Split('\')[-1]; net localgroup $rdpGroup Office /add | Out-Host } catch { Write-Host ('   Could not add Office to RDP group: ' + $_.Exception.Message) };" ^
"Write-Host '4/7 Checking printer exists...';" ^
"$printerInfo = Get-Printer -Name $printer -ErrorAction SilentlyContinue;" ^
"if (-not $printerInfo) { $printerInfo = Get-Printer | Where-Object { $_.ShareName -eq 'bx-proizv' } | Select-Object -First 1 };" ^
"if (-not $printerInfo) { Write-Host 'ERROR: HP printer was not found. Available printers:'; Get-Printer | Select-Object Name,ShareName,PrinterStatus | Format-Table -AutoSize; exit 2 };" ^
"Write-Host ('   Found printer: ' + $printerInfo.Name + ' / share: ' + $printerInfo.ShareName);" ^
"Write-Host '5/7 Stopping old bridge process if it exists...';" ^
"Get-CimInstance Win32_Process | Where-Object { $_.CommandLine -like '*PreAssemblyPrintBridge.ps1*' } | ForEach-Object { try { Stop-Process -Id $_.ProcessId -Force -ErrorAction Stop; Write-Host ('   Stopped PID ' + $_.ProcessId) } catch { Write-Host ('   Could not stop PID ' + $_.ProcessId + ': ' + $_.Exception.Message) } };" ^
"Start-Sleep -Seconds 1;" ^
"Write-Host '6/7 Creating autostart task at user logon...';" ^
"$argument = '-NoProfile -ExecutionPolicy Bypass -WindowStyle Hidden -File ' + [char]34 + $script + [char]34 + ' -PrinterName ' + [char]34 + $printerInfo.Name + [char]34 + ' -Port ' + $port;" ^
"$action = New-ScheduledTaskAction -Execute 'powershell.exe' -Argument $argument;" ^
"$trigger = New-ScheduledTaskTrigger -AtLogon;" ^
"Register-ScheduledTask -TaskName $taskName -Action $action -Trigger $trigger -Description 'PreAssembly Android print bridge' -RunLevel Highest -Force | Out-Null;" ^
"Write-Host '7/7 Starting bridge now...';" ^
"$out = Join-Path '%~dp0' 'PreAssemblyPrintBridge.stdout.log';" ^
"$err = Join-Path '%~dp0' 'PreAssemblyPrintBridge.stderr.log';" ^
"$proc = Start-Process -FilePath powershell.exe -ArgumentList $argument -WindowStyle Hidden -RedirectStandardOutput $out -RedirectStandardError $err -PassThru;" ^
"Set-Content -LiteralPath (Join-Path '%~dp0' 'PreAssemblyPrintBridge.pid') -Value $proc.Id -Encoding ASCII;" ^
"Start-Sleep -Seconds 2;" ^
"$health = Invoke-WebRequest -Uri ('http://127.0.0.1:' + $port + '/health') -UseBasicParsing -TimeoutSec 6;" ^
"Write-Host ('OK: bridge PID ' + $proc.Id);" ^
"Write-Host ('HTTP ' + $health.StatusCode + ': ' + $health.Content);" ^
"Write-Host '';" ^
"Write-Host 'From phone use bridge IP: 192.168.10.104';"

if errorlevel 1 (
    echo.
    echo Setup failed. Check the messages above and PreAssemblyPrintBridge.log.
) else (
    echo.
    echo Setup finished. Keep this BAT and generated PreAssemblyPrintBridge.ps1 on this computer.
)

pause
