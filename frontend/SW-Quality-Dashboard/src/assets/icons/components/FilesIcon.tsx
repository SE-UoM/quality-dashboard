import { MyIconProps } from "../types";

function FilesIcon({ width, height }: MyIconProps) {
  return (
    <svg
      xmlns="http://www.w3.org/2000/svg"
      width={width}
      height={height}
      version="1.1"
      viewBox="0 0 33.952 36.181"
      xmlSpace="preserve"
    >
      <g transform="translate(-130.779 -11.493)">
        <g
          fill="#404852"
          fillOpacity="1"
          transform="matrix(2.22002 0 0 2.22002 -392.385 -277.131)"
        >
          <path
            strokeWidth="0.035"
            d="M238.161 146.267a.67.67 0 01-.442-.374.677.677 0 01-.048-.561.821.821 0 01.465-.465c.11-.033 1.35-.043 5.441-.043 4.712 0 5.313-.006 5.428-.054a.803.803 0 00.393-.37c.064-.124.066-.263.066-4.295v-4.166h-3.84l-.176-.088a.741.741 0 01-.389-.484c-.025-.091-.037-.782-.038-2.005v-1.865h-2.656c-2.903 0-2.793-.007-3.013.201a.99.99 0 00-.165.222c-.062.122-.066.223-.066 1.718 0 1.026-.014 1.639-.038 1.729a.741.741 0 01-.39.484.695.695 0 01-.841-.138c-.228-.227-.23-.235-.217-2.169l.01-1.718.099-.277c.218-.618.619-1.064 1.19-1.326.454-.207.26-.197 3.861-.21 2.377-.009 3.365 0 3.506.027l.195.04 2.193 2.192c1.94 1.938 2.197 2.209 2.23 2.337.027.102.036 1.585.03 5.015l-.01 4.87-.097.274c-.21.597-.58 1.024-1.118 1.29-.518.258-.017.238-6.021.247-4.385.007-5.424 0-5.542-.038zm10.609-11.83c0-.004-.508-.515-1.128-1.136l-1.128-1.127V134.447h1.128c.62 0 1.128-.004 1.128-.01z"
          ></path>
          <text
            xmlSpace="preserve"
            style={{
              fontVariantLigatures: "normal",
              fontVariantCaps: "normal",
              fontVariantNumeric: "normal",
              fontVariantEastAsian: "normal",
            }}
            x="235.441"
            y="142.305"
            stroke="#404852"
            strokeDasharray="none"
            strokeDashoffset="0"
            strokeLinecap="round"
            strokeLinejoin="round"
            strokeOpacity="1"
            strokeWidth="0.634"
            fontFamily="Arial"
            fontSize="7.383"
            fontStretch="normal"
            fontStyle="normal"
            fontVariant="normal"
            fontWeight="bold"
            stopColor="#000"
          >
            <tspan
              style={{
                fontVariantLigatures: "normal",
                fontVariantCaps: "normal",
                fontVariantNumeric: "normal",
                fontVariantEastAsian: "normal",
              }}
              x="235.441"
              y="142.305"
              stroke="none"
              strokeWidth="0.634"
              fontFamily="Arial"
              fontSize="7.383"
              fontStretch="normal"
              fontStyle="normal"
              fontVariant="normal"
              fontWeight="bold"
            >
              {}
            </tspan>
          </text>
        </g>
      </g>
    </svg>
  );
}

export default FilesIcon;
