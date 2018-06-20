# ViewPagerHelper

** 一个可以帮助识别出ViewPager滑动方向的帮助类。**

```
public interface IViewPagerTrendListener {

    /**
     * 当一个页面被完全选定时调用。该方法会在{@link #onPrePageSelected(int)}之后被调用。
     *
     * @param selectedPosition 当前位置
     */
    void onFullPageSelected(int selectedPosition);

    /**
     * 当滑动方向被确定时调用
     *
     * @param direct           方向 {@link #DIRECT_LEFT} {@link #DIRECT_NONE} {@link #DIRECT_RIGHT}
     * @param selectedPosition 当前位置
     * @param nextPosition     下一个位置
     */
    void onDirectSelected(int direct, int selectedPosition, int nextPosition);

    /**
     * 当一个页面被选定时调用。该方法会在{@link #onFullPageSelected(int)}之前被调用。
     *
     * @param position 位置
     */
    void onPrePageSelected(int position);

    /**
     * 在当前方向下滑动的分数
     *
     * @param direct           方向
     * @param selectedPosition 当前位置
     * @param nextPosition     下一个位置
     * @param fraction         分数
     */
    void onFractionPage(int direct, int selectedPosition, int nextPosition, float fraction);
}
```

从第一页滑动到第二页时执行日志如下：

```
06-21 10:52:17.406  4861  4861 D MainActivity: onDirectSelected() called with: direct = [2], selectedPosition = [1], nextPosition = [2]
06-21 10:52:17.407  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.065740705]
06-21 10:52:17.425  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.17685187]
06-21 10:52:17.440  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.26666665]
06-21 10:52:17.442  4861  4861 D MainActivity: onPrePageSelected() called with: position = [2]
06-21 10:52:17.443  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.29444444]
06-21 10:52:17.459  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.47962964]
06-21 10:52:17.479  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.66388893]
06-21 10:52:17.496  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.7759259]
06-21 10:52:17.514  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.85925925]
06-21 10:52:17.531  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.9129629]
06-21 10:52:17.548  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.95092595]
06-21 10:52:17.566  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.9731481]
06-21 10:52:17.584  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.98703706]
06-21 10:52:17.601  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.9944445]
06-21 10:52:17.618  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.9981482]
06-21 10:52:17.636  4861  4861 D MainActivity: onFractionPage() called with: direct = [2], selectedPosition = [1], nextPosition = [2], positionOffset = [0.9990741]
06-21 10:52:17.740  4861  4861 D MainActivity: onFullPageSelected() called with: selectedPosition = [2]
```
