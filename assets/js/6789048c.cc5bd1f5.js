"use strict";(self.webpackChunkwebsite=self.webpackChunkwebsite||[]).push([[207],{3905:(e,t,n)=>{n.d(t,{Zo:()=>p,kt:()=>f});var r=n(7294);function a(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function i(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?i(Object(n),!0).forEach((function(t){a(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):i(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,r,a=function(e,t){if(null==e)return{};var n,r,a={},i=Object.keys(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||(a[n]=e[n]);return a}(e,t);if(Object.getOwnPropertySymbols){var i=Object.getOwnPropertySymbols(e);for(r=0;r<i.length;r++)n=i[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(a[n]=e[n])}return a}var c=r.createContext({}),s=function(e){var t=r.useContext(c),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},p=function(e){var t=s(e.components);return r.createElement(c.Provider,{value:t},e.children)},d="mdxType",u={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},m=r.forwardRef((function(e,t){var n=e.components,a=e.mdxType,i=e.originalType,c=e.parentName,p=l(e,["components","mdxType","originalType","parentName"]),d=s(n),m=a,f=d["".concat(c,".").concat(m)]||d[m]||u[m]||i;return n?r.createElement(f,o(o({ref:t},p),{},{components:n})):r.createElement(f,o({ref:t},p))}));function f(e,t){var n=arguments,a=t&&t.mdxType;if("string"==typeof e||a){var i=n.length,o=new Array(i);o[0]=m;var l={};for(var c in t)hasOwnProperty.call(t,c)&&(l[c]=t[c]);l.originalType=e,l[d]="string"==typeof e?e:a,o[1]=l;for(var s=2;s<i;s++)o[s]=n[s];return r.createElement.apply(null,o)}return r.createElement.apply(null,n)}m.displayName="MDXCreateElement"},1413:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>c,contentTitle:()=>o,default:()=>u,frontMatter:()=>i,metadata:()=>l,toc:()=>s});var r=n(7462),a=(n(7294),n(3905));const i={title:"Validate Ident Prefix",custom_edit_url:"https://github.com/bitlap/rolls/edit/master/docs/validate_ident_prefix.md"},o=void 0,l={unversionedId:"validate_ident_prefix",id:"validate_ident_prefix",title:"Validate Ident Prefix",description:"Installation using SBT (Recommended)",source:"@site/../rolls-docs/target/mdoc/validate_ident_prefix.md",sourceDirName:".",slug:"/validate_ident_prefix",permalink:"/rolls/docs/validate_ident_prefix",draft:!1,editUrl:"https://github.com/bitlap/rolls/edit/master/docs/validate_ident_prefix.md",tags:[],version:"current",frontMatter:{title:"Validate Ident Prefix",custom_edit_url:"https://github.com/bitlap/rolls/edit/master/docs/validate_ident_prefix.md"},sidebar:"tutorialSidebar",previous:{title:"ResultSetX",permalink:"/rolls/docs/resultset_x"}},c={},s=[{value:"Installation using SBT (Recommended)",id:"installation-using-sbt-recommended",level:2},{value:"What will be verified ?",id:"what-will-be-verified-",level:2}],p={toc:s},d="wrapper";function u(e){let{components:t,...n}=e;return(0,a.kt)(d,(0,r.Z)({},p,n,{components:t,mdxType:"MDXLayout"}),(0,a.kt)("h2",{id:"installation-using-sbt-recommended"},"Installation using SBT (Recommended)"),(0,a.kt)("p",null,"If you are building with sbt, add the following to your ",(0,a.kt)("inlineCode",{parentName:"p"},"build.sbt"),":"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-scala"},'autoCompilerPlugins := true\naddCompilerPlugin("org.bitlap" %% "rolls-compiler-plugin" % "<version>")\n\nlazy val reader = scala.io.Source.fromFile("config.properties")\nlazy val config = {\n  val ret = reader.getLines().toList.map(p => s"-P:RollsCompilerPlugin:$p")\n  reader.close()\n  ret\n}\n\nscalacOptions ++= config\n')),(0,a.kt)("p",null,"Add the following properties to ",(0,a.kt)("strong",{parentName:"p"},"config.properties"),":"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-properties"},"# Multiple annotations split by '|'\nvalidateIdentPrefix=caliban.schema.Annotations.GQLDescription\nvalidateShouldStartsWith=star\n")),(0,a.kt)("h2",{id:"what-will-be-verified-"},"What will be verified ?"),(0,a.kt)("ul",null,(0,a.kt)("li",{parentName:"ul"},"Validate parameter name within primary constructor:",(0,a.kt)("ul",{parentName:"li"},(0,a.kt)("li",{parentName:"ul"},"when parameter type is a case class or function type and has annotations."),(0,a.kt)("li",{parentName:"ul"},"annotations can be on parameter or parameter type."))),(0,a.kt)("li",{parentName:"ul"},"Validate case class name (use ",(0,a.kt)("inlineCode",{parentName:"li"},".capitalize")," to validate case classes):",(0,a.kt)("ul",{parentName:"li"},(0,a.kt)("li",{parentName:"ul"},"when primary constructor or type constructor has annotations."),(0,a.kt)("li",{parentName:"ul"},"when parameter type is function type or case class and has annotations.")))),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre",className:"language-scala"},'import caliban.schema.Annotations.GQLDescription\n\nfinal case class StarDictInput(\n  @GQLDescription("dictName")\n  dictName: Option[String],\n  @GQLDescription("dictCode")\n  dictCode: Option[String],\n  @GQLDescription("starDictPayload")\n  starDictPayload: StarDictPayload,\n  @GQLDescription("dictFunction")\n  dictFunction: String => StarDictPayload\n)\n\nfinal case class StarDictPayload(\n  @GQLDescription("id")\n  id: Option[String],\n  @GQLDescription("name")\n  name: Option[String]\n)\n')),(0,a.kt)("p",null,"The above code will provide compiler error:"),(0,a.kt)("pre",null,(0,a.kt)("code",{parentName:"pre"},"parameter names of the primary constructor don't startsWith star in StarDictInput\nExpected: starDictFunction \nActual: dictFunction\n\n  final case class StarDictInput(\n")),(0,a.kt)("blockquote",null,(0,a.kt)("p",{parentName:"blockquote"},"Because dictFunction does not startsWith ",(0,a.kt)("inlineCode",{parentName:"p"},"star"))))}u.isMDXComponent=!0}}]);