
%%
%line
%class AnalizadorLexico
%type  UnidadLexica
%unicode

%{
  private ALexOperations ops;
  public String lexema() {return yytext();}
  public int fila() {return yyline+1;}
%}

%eofval{
  return ops.unidadEOF();
%eofval}

%init{
  ops = new ALexOperations(this);
%init}

%unicode

letra  = [A-Za-z]
digito = [0-9]
varName = {letra}({letra}|{digito})*
numberR = {int}{dot}{digito}+
float = ({numberR}|{int})[e,E]{int}
int = [\+,\-]?{digito}+
separador = [\ \t\r\b\n\012]
ampSep = "&&"
num = num
bool = bool
true = true
false = false
and = and
or = or
not = not
assign = \=
dot = \.
semicolon = ;
popen = \(
pclose = \)
add = \+
sub = \-
mul = \*
div = /
eq = \=\=
ne = !\=
le = <\=
lt = <
ge = >\=
gt = >

%%

{separador} {}
{varName}   {return ops.unidadVarName();}
{float}     {return ops.unidadFloat();}
{numberR}   {return ops.unidadReal();}
{int}       {return ops.unidadInt();}
{num}       {return ops.unidadNum();}
{bool}      {return ops.unidadBool();}
{true}      {return ops.unidadTrue();}
{false}     {return ops.unidadFalse();}
{and}       {return ops.unidadAnd();}
{or}        {return ops.unidadOr();}
{not}       {return ops.unidadNot();}
{assign}    {return ops.unidadIgual();}
{dot}       {return ops.unidadPunto();}
{semicolon} {return ops.unidadPuntoComa();} 
{ampSep}    {return ops.unidadAmpSep();}
{add}       {return ops.unidadMas();}
{sub}       {return ops.unidadMenos();} 
{mul}       {return ops.unidadPor();} 
{div}       {return ops.unidadDiv();} 
{popen}     {return ops.unidadPAp();} 
{pclose}    {return ops.unidadPCierre();} 
{gt}        {return ops.unidadGT();} 
{ge}        {return ops.unidadGE();}
{lt}        {return ops.unidadLT();}
{le}        {return ops.unidadLE();}
{ne}        {return ops.unidadNE();}
{eq}        {return ops.unidadEQ();}

[^]          {ops.error();}  