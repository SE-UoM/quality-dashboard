import "swiper/css";
import { Swiper, SwiperSlide } from "swiper/react";
import Dashboard1 from "../features/MainDashboard/Screen_1";
import Dashboard2 from "../features/MainDashboard/Screen_2";
import Dashboard3 from "../features/MainDashboard/Screen_3";
import Dashboard4 from "../features/MainDashboard/Screen_4";
import { Navigation } from "swiper/modules";

function MainCarousel() {
  return (
    <Swiper modules={[Navigation]} slidesPerView={1}>
      <SwiperSlide className="swiper-slide" key="dashboard1">
        <Dashboard1 />
      </SwiperSlide>
      <SwiperSlide className="swiper-slide" key="dashboard2">
        <Dashboard2 />
      </SwiperSlide>
      <SwiperSlide className="swiper-slide" key="dashboard3">
        <Dashboard3 />
      </SwiperSlide>
      <SwiperSlide className="swiper-slide" key="dashboard4">
        <Dashboard4 />
      </SwiperSlide>
    </Swiper>
  );
}

export default MainCarousel;
